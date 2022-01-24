let copy = {
    id: 0,
    form: null,

    read(document) {
        this.id = document.getElementById('copyId');
        this.form = document.getElementById('writingOff');
        document.querySelector('#submitWritingOff').style.display = "none";
    },

    getInfo() {
        if (!isEmpty(this.id.value.trim())) {
            $.get("/library/getCopyInfo", {id: copy.id.value}, function (responseJson) {
                if(![responseJson['valid']]){
                    alert("Не удалось получить информацию о книгах");
                    return false;
                }
                if (responseJson['id'] === 0) {
                    showError(copy.id, "Книга выдана читателю");
                    $('#info').html('');
                } else if (responseJson['id'] === -1) {
                    showError(copy.id, "Такой копии не существует");
                    $('#info').html('');
                } else {
                    showSuccess(copy.id);
                    $('#info').html('Наименование: ' + responseJson['name'] + '<br>' +
                        'Повреждения: ' + (responseJson['damage'] || ""));
                    document.querySelector('#submitWritingOff').style.display = "";
                }
            });
        } else {
            showError(this.id, "Поле не может быть пустым");
        }

    },

    isValid() {
        return this.id.parentElement.classList.contains('success');
    }

}