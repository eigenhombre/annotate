(ns annotate.core
  "
  This modest example, assembled and translated from Java snippets on
  StackOverflow, shows how to add whitespace at the bottom of an image
  and add an annotation.
  "
  (:require [mikera.image.core :as i])
  (:import [org.imgscalr Scalr]
           [java.awt.image BufferedImage]
           [java.awt Color Font RenderingHints]
           [java.awt.font TextLayout FontRenderContext])
  (:gen-class))

(defn load [s]
  (i/load-image s))

(defn dims
  "
  Get width, height of `img`.
  "
  [img]
  [(.getWidth img) (.getHeight img)])

(defn annotated-img
  "
  Return new `BufferedImage` with string `s` written into some extra
  space added to the bottom.
  "
  [img s]
  (let [[w h] (dims img)
        text-region-height 40
        h' (+ text-region-height h)
        img' (BufferedImage. w h' (.getType img))
        g (.getGraphics img')
        font (Font. "Arial" Font/PLAIN 22)]
    (doto g
      (.setColor Color/WHITE)
      (.fillRect 0 0 w h')
      (.drawImage img 0 0 nil)
      (.setPaint Color/BLACK)
      (.setFont font)
      (.setRenderingHint RenderingHints/KEY_TEXT_ANTIALIASING
                         RenderingHints/VALUE_TEXT_ANTIALIAS_ON)
      (.drawString s 5 (+ h 30))
      (.dispose))
    img'))

(comment

  (def test-image-file "example.jpg")
  (def img (i/load-image test-image-file))
  [(.getWidth img) (.getHeight img)]

  (i/show img)
  (i/show (annotated-img img
                         (apply str
                                ["© 2020 NPX Designs, Inc."]))))
(comment
  (require '[marginalia.core :as marg])
  (marg/run-marginalia ["src"])

  (clojure.java.shell/sh "open"
                         "docs/uberdoc.html"))
