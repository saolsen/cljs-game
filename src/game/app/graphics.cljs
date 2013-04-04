;; Uses threejs
(ns game.graphics)

(defn cube
  []
  (let [material (js/THREE.MeshBasicMaterial.
                  (clj->js {:color 0}))
        geom (js/THREE.CubeGeometry. 10 10 10)
        cube (js/THREE.Mesh. geom material)]
    (aset (.-position cube) "y" 150)
    cube))

(defn setup-floor
  "creates a floor"
  []
  (let [material (js/THREE.MeshBasicMaterial.
                  (clj->js {:color 13369344
                            :side js/THREE.DoubleSide}))
        geom (js/THREE.PlaneGeometry. 1000 1000)
        floor (js/THREE.Mesh. geom material)]
    ;(aset (.-position floor) "y" 0)
    (aset (.-rotation floor) "x" (/ js/Math.PI 2))
    floor))

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
        camera (js/THREE.PerspectiveCamera. view-angle aspect near far)
        container (.createElement js/document "div")
        stats (js/Stats.)
        style (.-style (.-domElement stats))
        controls (THREE.PointerLockControls. camera)]
    (.add scene camera)
    (.add scene (.getObject controls))
    (aset controls "enabled" true)
    (.set (.-position camera)  0 150 400)
    (.lookAt camera (.-position scene))
    (.setSize renderer screen-width screen-height)
    (.appendChild js/document.body container)
    (.appendChild container (.-domElement renderer))
    (.WindowResize js/THREEx renderer camera)
    (.setMode stats 0)
    (aset style "position" "absolute")
    (aset style "left" "0px")
    (aset style "top" "0px")
    (.appendChild js/document.body (.-domElement stats))
    {:camera camera
     :scene scene
     :renderer renderer
     :stats stats
     :controls controls}))
