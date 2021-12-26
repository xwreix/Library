const surname = document.querySelector('#surname');
const patronymic = document.querySelector('#patronymic');
const email = document.querySelector('#email');

const form = document.querySelector('#addReader');

form.addEventListener('submit', function (e) {
    e.preventDefault();

    let isSurnameValid = checkSurname(),
        isEmailValid = checkEmail(),
        isPatronymicValid = checkPatronymic();

    let isFormValid = isSurnameValid &&
        isEmailValid && isPatronymicValid;

    if (isFormValid) {
        form.submit();
    }
});

const isRequired = value => value === '';

const isBetween = (length, min, max) => !(length < min || length > max);

const isEmailValid = (email) => {
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
};

const showError = (input, message) => {
    const formField = input.parentElement;

    formField.classList.remove('success');
    formField.classList.add('error');

    const error = formField.querySelector('small');
    error.textContent = message;
};

const showSuccess = (input) => {
    const formField = input.parentElement;

    formField.classList.remove('error');
    formField.classList.add('success');

    const error = formField.querySelector('small');
    error.textContent = '';
};

const checkSurname = () => {
    let valid = false;
    const min = 2,
        max = 50;
    const surnameCheck = surname.value.trim();

    if (isRequired(surnameCheck)) {
        showError(surname, 'Поле не может быть пустым.');
    } else if (!isBetween(surnameCheck.length, min, max)) {
        showError(surname, 'Поле не может содержать более 50 символов')
    } else {
        showSuccess(surname);
        valid = true;
    }
    return valid;
};

const checkPatronymic = () => {
    let valid = false;
    const min = 0,
        max = 50;
    const patronymicCheck = patronymic.value.trim();

    if (isRequired(patronymicCheck)) {
        patronymic.setAttribute('value', null);
        showSuccess(surname);
        valid = true;
    } else if (!isBetween(patronymicCheck.length, min, max)) {
        showError(surname, 'Поле не может содержать более 50 символов')
    } else {
        showSuccess(surname);
        valid = true;
    }
    return valid;
};

const checkEmail = () => {
    let valid = false;
    const emailCheck = email.value.trim();

    if (isRequired(emailCheck)) {
        showError(email, 'Поле не может быть пустым.');
    } else if (isEmailValid(email)) {
        showError(email, 'Email введён неверно')
    } else {
        showSuccess(email);
        valid = true;
    }
    return valid;
};

function sendData() {
    const XHR = new XMLHttpRequest();
    const FD = new FormData(form);

    XHR.addEventListener('error', function (event) {
        alert('Не удалось зарегистрировать читателя');
    });

    XHR.open("POST", "/library/addReader");
    XHR.send(FD);
}

