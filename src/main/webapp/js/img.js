const form = document.querySelector('#Ing');

form.addEventListener('submit', function () {
    img();
})

function img() {
    let fd = new FormData();
    fd.append('file', document.getElementById('file_i').files[0]);
    let req;
    if (window.ActiveXObject) {
        req = new ActiveXObject();
    } else {
        req = new XMLHttpRequest()
    }
    req.open('post', 'Image', true);
    req.send(fd);
}
