let copy = {
    id: 0,
    form: null,

    read(document) {
        this.id = document.getElementById('copyId');
        this.form = document.getElementById('writingOff');
        document.querySelector('#submitWritingOff').style.display = "none";
    },

    getInfo() {
        if(!isEmpty(this.id.value.trim())){
            $.get("/library/getCopyInfo", {id: copy.id.value}, function (responseJson) {
                if (responseJson['id'] === 0) {
                    showError(copy.id, "Книга выдана читателю");
                    $('#info').html('');
                } else if (responseJson['id'] === -1) {
                    showError(copy.id, "Такой копии не существует");
                    $('#info').html('');
                } else {
                    showSuccess(copy.id);
                    let name = responseJson['name'];
                    let damage = responseJson['damage'];
                    if (undefined === damage){
                        damage = "";
                    }
                    $('#info').html('Наименование: ' + name + '<br>' +
                        'Повреждения: ' + damage);
                    document.querySelector('#submitWritingOff').style.display = "";
                }
            });
        } else {
            showError(this.id, "Поле не может быть пустым");
        }

    },

    isValid(){
        return this.id.parentElement.classList.contains('success');
    }

}