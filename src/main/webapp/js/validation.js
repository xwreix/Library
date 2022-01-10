const isEmpty = value => value === '';

const isNotBetween = (length, max) => length > max;

const showSuccess = (input) => {
    let formField = input.parentElement;

    const error = formField.querySelector('small');
    error.textContent = '';

    if(input.classList.contains('authorName') ){
        formField = input;
    }

    formField.classList.remove('error');
    formField.classList.add('success');

};

const showError = (input, message) => {
    const formField = input.parentElement;

    formField.classList.remove('success');
    formField.classList.add('error');

    const error = formField.querySelector('small');
    error.textContent = message;
};

const checkRequired = (param) => {
    let valid = false,
        val = param.value.trim();

    if(isEmpty(val)) {
        showError(param, "Поле обязательно к заполнению");
    } else {
        showSuccess(param);
        valid = true;
    }

    return valid;
}

const checkLength = (param, max) => {
    let valid = false,
        val = param.value.trim();

    if(isNotBetween(val.length, max)){
        showError(param, "Введено слишком длинное значение");
    } else {
        showSuccess(param);
        valid = true;
    }

    return valid;
}