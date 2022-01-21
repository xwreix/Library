$(document).ready(function () {

    $($('#calculate')).click(function () {
        profitability.read(document);

        if (profitability.isValid()) {
            profitability.calculate();
        }
    });
});