const form = document.querySelector('#newReader');

const surname = document.querySelector('#surname');
const name = document.querySelector('#name');
const email = document.querySelector('#email');
const dateOfBirth = document.querySelector('#dateOfBirth');
const patronymic = document.querySelector('#patronymic');
const passportNumber = document.querySelector('#passportNumber');
const address = document.querySelector('#address');

form.addEventListener('submit', function (event) {
    event.preventDefault();

    let isSurnameValid = checkRequired(surname),
        isNameValid = checkRequired(name),
        isEmailValid = checkRequired(email),
        isDateValid = checkRequired(dateOfBirth),
        isPatronymicValid, isPassportValid;

    if(isSurnameValid && isNameValid && isEmailValid && isDateValid){
        isSurnameValid = checkLength(surname);
        isNameValid = checkLength(name);
        isEmailValid = checkEmail();
        isDateValid = checkDate();
        isPatronymicValid = checkLength(patronymic);
        isPassportValid = checkPassport();
    }

    if(isSurnameValid && isNameValid && isEmailValid && isDateValid && isPatronymicValid
                && isPassportValid){
        setNull();
        form.submit();
    }

});

const isEmpty = value => value === '';

const isNotBetween = (length, max) => length > max;

const isEmailValid = (email) => {
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
};

const showSuccess = (input) => {
    const formField = input.parentElement;

    formField.classList.remove('error');
    formField.classList.add('success');

    const error = formField.querySelector('small');
    error.textContent = '';
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
        showError(param, "Поле не может быть пустым");
    } else {
        showSuccess(param);
        valid = true;
    }

    return valid;
}

const checkLength = (param) => {
    let valid = false,
        val = param.value.trim(),
        max = 50;

    if(isNotBetween(val.length, max)){
        showError(param, "Поле не может быть содержать более 50 символов");
    } else {
        showSuccess(param);
        valid = true;
    }

    return valid;
}


const checkEmail = () => {
    let valid = false;
    const val = email.value.trim();

    if (!isEmailValid(val)){
        showError(email, "Email введён некорректно");
    } else {
        showSuccess(email);
        valid = true;
    }

    return valid;
};

const checkDate = () => {
    let valid = false;
    const val = new Date(dateOfBirth.value);

    if((val.getFullYear() <= 1920) || (val.getFullYear() >=  2016)){
        showError(dateOfBirth, "Недопустимый возраст");
    } else {
        showSuccess(dateOfBirth);
        valid = true;
    }

    return valid;
};

const checkPassport = () => {
    let valid = false;
    const val = passportNumber.value.trim();
    const re = /[A-Z]{2}[0-9]{7}/;
    if(!re.test(val) && !isEmpty(val)){
        showError(passportNumber, "Номер паспорта введён некорректно");
    } else {
        showSuccess(passportNumber);
        valid = true;
    }

    return valid;
}

function setNull(){
    if(isEmpty(patronymic.value.trim())){
        patronymic.setAttribute("value", null);
    }
    if(isEmpty(passportNumber.value.trim())){
        passportNumber.setAttribute("value", null);
    }
    if(isEmpty(address.value.trim())){
        address.setAttribute("value", null);
    }
}



