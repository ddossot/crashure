(ns net.dossot.crashure.core
  (:require [net.dossot.crashure.config :as config])
  (:import org.crsh.standalone.Bootstrap
           org.crsh.vfs.Path
           java.util.Properties))

(defn- set-conf-paths
  [bootstrap options]
  (doseq [cp (:conf-paths options)]
    (.addToConfPath bootstrap cp)))

(defn- set-cmd-paths
  [bootstrap options]
  (let [cps (or
              (:cmd-paths options)
              [(Path/get "/crash/commands/")])]
    (doseq [cp cps]
      (.addToCmdPath bootstrap cp))))

(defn- set-attributes
  [bootstrap options]
  (when-let [attributes (:attributes options)]
    (.setAttributes bootstrap attributes)))

(defn make-bootstrap
  [options]
  (let [classloader (or (:classloader options)
                      (.getContextClassLoader (Thread/currentThread)))
        properties (config/options->Properties options)
        configurator (or (:configurator options)
                       identity)]

    (doto (Bootstrap. classloader)
      (.setConfig properties)
      (set-conf-paths options)
      (set-cmd-paths options)
      (set-attributes options)
      (configurator))))

(defn start
  [options]
  (doto (make-bootstrap options)
    (.bootstrap)))

(defn stop
  [^Bootstrap bootstrap]
  {:pre [(instance? Bootstrap bootstrap)]}
  (.shutdown bootstrap))
