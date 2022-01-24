let profitability = {
    startDate: null,
    finishDate: null,

    read(document) {
        this.startDate = document.querySelector('#startDate');
        this.finishDate = document.querySelector('#finishDate');
    },

    isValid() {
        let today = new Date();
        let valid = true;

        if (Date.parse(this.startDate.value) > today.getTime()) {
            showError(this.startDate, "Некорректная дата");
            valid = false;
        } else {
            showSuccess(this.startDate);
        }

        if (Date.parse(this.finishDate.value) > today.getTime()) {
            showError(this.finishDate, "Некорректная дата");
            valid = false;
        } else {
            showSuccess(this.finishDate);
        }

        if (valid) {
            if (Date.parse(this.startDate.value) > Date.parse(this.finishDate.value)) {
                showError(this.startDate, "Начальная дата не может быть больше конечной");
                valid = false;
            }
        }

        return valid;
    },

    calculate() {
        $.get("/library/calcProfitability", {
            start: profitability.startDate.value,
            finish: profitability.finishDate.value
        }, function (responseJson) {
            if (!responseJson) {
                alert("Не удалось рассчитать доходность");
                return false;
            }
            $('#info').html('Выручка: ' + responseJson['revenue'] + '<br>' +
                'Количество книг: ' + responseJson['booksAmount'] + '<br>' +
                'Количество читателей: ' + responseJson['readersAmount']);
        });
    }

}