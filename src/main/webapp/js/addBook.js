const button_add = document.querySelector('.dynamic_fields .add');
const form = document.querySelector('#newBook');

form.addEventListener('submit', function (event){
    event.preventDefault();
    alert("ff");

})

button_add.addEventListener('click', function () {
    let students = document.querySelector('.dynamic_fields .authors');

    let element = document.querySelector('.author').cloneNode(true);
    element.classList.add('authors');
    element.classList.remove('author');
    students.appendChild(element);

});

document.addEventListener('click', function (el){
    if(el.target && el.target.classList.contains('remove')){
        let child = el.target.closest('.table');

        child.parentNode.removeChild(child);
    }
});
