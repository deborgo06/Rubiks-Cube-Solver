let scene = new THREE.Scene();
let camera = new THREE.PerspectiveCamera(75, window.innerWidth/window.innerHeight);
let renderer = new THREE.WebGLRenderer();

renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);
camera.position.z = 5;

let cube = new THREE.Mesh(
    new THREE.BoxGeometry(),
    new THREE.MeshNormalMaterial()
);

scene.add(cube);

function animate() {
    requestAnimationFrame(animate);
    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;
    renderer.render(scene, camera);
}
animate();

function solve() {
    fetch("http://localhost:8080/solve", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({
            scramble: document.getElementById("scramble").value,
            method: document.getElementById("method").value
        })
    })
    .then(res => res.json())
    .then(data => {
        document.getElementById("output").innerText = data.solution;
    });
}
