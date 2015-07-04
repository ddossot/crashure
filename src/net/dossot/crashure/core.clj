(ns net.dossot.crashure.core
  (:require [net.dossot.crashure.config :as config])
  (:import org.crsh.standalone.Bootstrap
           org.crsh.vfs.Path
           java.util.Properties))

(defn- set-conf-paths
  [server options]
  (doseq [cp (:conf-paths options)]
    (.addToConfPath server cp)))

(defn- set-cmd-paths
  [server options]
  (let [cps (or
              (:cmd-paths options)
              [(Path/get "/crash/commands/")])]
    (doseq [cp cps]
      (.addToCmdPath server cp))))

(defn make-server
  [options]
  (let [classloader (.getContextClassLoader (Thread/currentThread))
        properties (config/options->Properties options)
        configurator (or (:configurator options) identity)]

    (doto (Bootstrap. classloader)
      (.setConfig properties)
      (set-cmd-paths options)
      (configurator))))

(defn start
  [options]
  (doto (make-server options)
    (.bootstrap)))

(defn stop
  [^Bootstrap server]
  {:pre [(instance? Bootstrap server)]}
  (.shutdown server))
