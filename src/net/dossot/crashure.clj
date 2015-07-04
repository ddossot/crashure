(ns net.dossot.crashure
  (:require [potemkin.namespaces :refer [import-vars]]
            [net.dossot.crashure.core]))

(import-vars
  [net.dossot.crashure.core
   start
   stop])
