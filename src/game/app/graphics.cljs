;; Uses threejs
(ns game.graphics)

(defn setup-threejs
  "sets up the screen,
   returns the camera, scene and renderer"
  []
  (let [screen-width (.-innerWidth js/window)
        screen-height (.-innerHeight js/window)
        view-angle 45
        aspect (/ screen-width screen-height)
        near 0.1
        far 20000
        scene (js/THREE.Scene.)
        renderer (js/THREE.WebGLRenderer. (clj->js {:antialias true}))
        camera (js/THREE. view-angle aspect near far)
        container (.createElement js/document "div")]
    (.add scene camera)
    (.set camera.position 0 150 400)
    (.lookAt camera (.-position scene))
    (.setSize renderer screen-width screen-height)
    (.appendChild js/document.body container)
    (.appendChild container (.-domElement renderer))
    (.WindowResize js/THREEx renderer camera))
  {:camera camera
   :scene scene
   :renderer renderer})
