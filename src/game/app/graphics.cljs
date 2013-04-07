;; Uses threejs
(ns game.graphics)

(defn cube
  "makes a cube at 10 10 10"
  []
  (let [material (js/THREE.MeshBasicMaterial.
                  (clj->js {:color 0}))
        geom (js/THREE.CubeGeometry. 10 10 10)
        cube (js/THREE.Mesh. geom material)]
    (aset (.-position cube) "y" 250)
    cube))

(defn tree
  "makes a tree like nate designed
   to start I'm going to try and exactly replicate what nate had.
   It's 4 spheres, connected with cylinders to a trunk which is
   another cylinder.
   wow, this is much harder than I expected it would be.
  "
  ([spacing leaf-height trunk-height leaf-size]
     (let [sphere-material (js/THREE.MeshBasicMaterial.
                            (clj->js {:color 26163 ;greenish
                                      ;:wireframe true
                                      })) 
           sphere-geometry (js/THREE.SphereGeometry. leaf-size
                                                     leaf-size
                                                     leaf-size)
           tree-material (js/THREE.MeshBasicMaterial.
                              (clj->js {:color 7086848 ;brown
                                        ;:wireframe true
                                        }))
           trunk-radius (/ spacing 5)
           trunk-geometry (js/THREE.CylinderGeometry. trunk-radius
                                                      trunk-radius
                                                      trunk-height
                                                      20
                                                      false)
           branch-geometry (js/THREE.CylinderGeometry. 5
                                                       5
                                                       40
                                                       12
                                                       false)
           trunk (js/THREE.Mesh. trunk-geometry tree-material)
           container (js/THREE.Object3D.)
           s (/ spacing 2)
           branch-height (+ trunk-height
                            (/ (- leaf-height trunk-height) 2))]
       (.applyMatrix branch-geometry
                     (.makeRotationX (js/THREE.Matrix4.)
                                     (/ js/Math.PI 2)))
       (doseq [[x z] [[(- s) (- s)] [(- s) s] [s (- s)] [s s]]]
         (let [new-sphere (js/THREE.Mesh. sphere-geometry sphere-material)
               branch (js/THREE.Mesh. branch-geometry tree-material)]
           (aset (.-position branch) "y" branch-height)
           (aset (.-position branch) "x" (/ x 2))
           (aset (.-position branch) "z" (/ z 2))
           (aset (.-position new-sphere) "x" x)
           (aset (.-position new-sphere) "z" z)
           (aset (.-position new-sphere) "y" leaf-height)
           (.lookAt branch (.-position new-sphere))
           (.add container new-sphere)
           (.add container branch)))
       (aset (.-position trunk) "y" (/ trunk-height 2))
       (.add container trunk)
       container))
  ([] (tree 60 80 60 25)))

(defn setup-floor
  "creates a floor"
  []
  (let [material (js/THREE.MeshBasicMaterial.
                  (clj->js {:color 12632256 ;grey
                            ;:wireframe true
                            :side js/THREE.DoubleSide}))
        geom (js/THREE.PlaneGeometry. 1000 1000 100 100)
        floor (js/THREE.Mesh. geom material)]
    ;(aset (.-position floor) "y" 0)
    (aset (.-rotation floor) "x" (/ js/Math.PI 2))
    floor))

(defn camera-objects
  "objects used for the players camera."
  [camera]
  (let [pitch-object (js/THREE.Object3D.)
        yaw-object (js/THREE.Object3D.)]
    (.add pitch-object camera)
    (aset (.-position yaw-object) "y" 10)
    (.add yaw-object pitch-object)
    {:pitch-object pitch-object
     :yaw-object yaw-object}))

(defn setup-threejs
  "sets up the screen,
   returns the camera, scene and renderer"
  []
  (let [screen-width (.-innerWidth js/window)
        screen-height (.-innerHeight js/window)
        view-angle 75
        aspect (/ screen-width screen-height)
        near 1
        far 500
        scene (js/THREE.Scene.)
        renderer (js/THREE.WebGLRenderer. (clj->js {:antialias true}))
        camera (js/THREE.PerspectiveCamera. view-angle aspect near far)
        container (.createElement js/document "div")
        stats (js/Stats.)
        style (.-style (.-domElement stats))]
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
     :stats stats}))
