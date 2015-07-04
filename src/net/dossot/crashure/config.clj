(ns net.dossot.crashure.config
  (:require [schema.core :refer :all])
  (:import org.crsh.standalone.Bootstrap
           org.crsh.vfs.Path
           org.crsh.vfs.spi.FSDriver
           java.io.File
           java.util.Properties))

;; TODO add SSH support

(def TelnetConfigSchema
  "Schema for configuring CRaSH telnet connector:

   :port - the port to listen on"
  {(required-key :port) (both (pred integer?) (pred pos?))})

(def CrashConfigSchema
  "Schema for configuring CRaSH:

   :configurator   - a function called with the CRaSH Bootstrap instance
   :commands-paths - a vector of paths from which commands are loaded
                     (defaults to \"/crash/commands/\")
   :telnet         - the configuration of the telnet connector (optional)"
  {(optional-key :configurator)    (=> Any Bootstrap)
   (optional-key :commands-paths)  [(either Str File FSDriver)]
   (optional-key :telnet)          TelnetConfigSchema})

(defn- set-telnet
  [props telnet-conf]
  (when telnet-conf
    (.setProperty
      props
      "crash.telnet.port"
      (str (:port telnet-conf)))))

(defn options->Properties
  [options]
  (validate CrashConfigSchema options)
  (doto (Properties.)
    (set-telnet (:telnet options))))
