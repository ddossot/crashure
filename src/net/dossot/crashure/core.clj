(ns net.dossot.crashure.core
  (:require [net.dossot.crashure.config :as config])
  (:import org.crsh.standalone.Bootstrap
           org.crsh.vfs.Path
           java.util.Properties))

(defn- set-command-paths
  [server options]
  (let [cps (or
              (:commands-paths options)
              ["/crash/commands/"])]
    (doseq [cp cps]
      (.addToCmdPath server
        (Path/get cp)))))

(defn make-server
  [options]
  (let [classloader (.getContextClassLoader (Thread/currentThread))
        properties (config/options->Properties options)
        configurator (or (:configurator options) identity)]

    (doto (Bootstrap. classloader)
      (.setConfig properties)
      (set-command-paths options)
      (configurator))))

(defn start
  [options]
  (doto (make-server options)
    (.bootstrap)))

(defn stop
  [^Bootstrap server]
  {:pre [(instance? Bootstrap server)]}
  (.shutdown server))
