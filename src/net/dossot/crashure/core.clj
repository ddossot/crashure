(ns net.dossot.crashure.core
  (:require [net.dossot.crashure.config :as config])
  (:import org.crsh.standalone.Bootstrap
           org.crsh.vfs.Path
           java.util.Properties))

(defn- set-command-paths
  [server conf]
  (let [cps (or
              (:commands-path conf)
              ["/crash/commands/"])]
    (doseq [cp cps]
      (.addToCmdPath server
        (Path/get cp)))))

(defn make-server
  [conf]
  (let [classloader (.getContextClassLoader (Thread/currentThread))
        properties (config/conf->Properties conf)]

    (doto (Bootstrap. classloader)
      (set-command-paths conf)
      (.setConfig properties))))

(defn start
  [conf]
  (doto (make-server conf)
    (.bootstrap)))

(defn stop
  [^Bootstrap server]
  {:pre [(instance? Bootstrap server)]}
  (.shutdown server))
