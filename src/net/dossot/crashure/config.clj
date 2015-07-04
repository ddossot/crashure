(ns net.dossot.crashure.config
  (:require [schema.core :refer [required-key optional-key
                                 both pred Str
                                 validate]])
  (:import java.util.Properties))

(def TelnetConfigSchema
  "Schema for configuring CRaSH telnet connector"
  {(required-key :port) (both (pred integer?) (pred pos?))})

(def CrashConfigSchema
  "Schema for configuring CRaSH."
  {(optional-key :commands-paths)  [Str]
   (optional-key :telnet)          TelnetConfigSchema})

(defn- set-telnet
  [props telnet-conf]
  (when telnet-conf
    (.setProperty
      props
      "crash.telnet.port"
      (str (:port telnet-conf)))))

(defn conf->Properties
  [conf]
  (validate CrashConfigSchema conf)
  (doto (Properties.)
    (set-telnet (:telnet conf))))
