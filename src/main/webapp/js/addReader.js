const form = document.querySelector('#newReader');

const surname = document.querySelector('#surname');
const name = document.querySelector('#name');

form.addEventListener('submit', function (event) {
    event.preventDefault();

    let isSurnameValid = checkRequiredString(surname),
        isNameValid = checkRequiredString(name);

});

const checkRequiredString = (param) => {
    let valid = false,
        val = param.value.trim(),
        min = 1,
        max = 50;

    if(isEmpty(val)){
        showError(param,"Поле не может быть пустым");
    } else if(isNotBetween(val.length, min, max)) {
        showError(param, "Поле не может быть содержать более 50 символов");
    } else {
        showSuccess(param);
        valid = true;
    }

    return valid;
}

const isEmpty = value => value === '';

const isNotBetween = (length, min, max) => length < min || length > max;

const showSuccess = (input) => {
    const formField = input.parentElement;

    formField.classList.remove('error');
    formField.classList.add('success');

    const error = formField.querySelector('small');
    error.textContent = '';
}

const showError = (input, message) => {
    const formField = input.parentElement;

    formField.classList.remove('success');
    formField.classList.add('error');

    const error = formField.querySelector('small');
    error.textContent = message;
};