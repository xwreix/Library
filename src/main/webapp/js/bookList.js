$(document).ready(function (){
    $('#books').after('<div id="nav"></div>');
    let rowsShown = 3;
    let rows = $('#books tbody tr');
    let rowsTotal = rows.length;
    let numPages = rowsTotal/rowsShown;

    for(let i = 0; i<numPages; i++){
        let pageNum = i+1;
        $('#nav').append('<a href="#" rel="'+i+'">'+pageNum+'</a> ');
    }

    rows.hide();
    rows.slice(0, rowsShown).show();
    $('#nav a').bind('click', function (){
        $('#nav a').removeClass('active');
        $(this).addClass('active');
        let currPage = $(this).attr('rel');
        let startItem = currPage * rowsShown;
        let endItem = startItem + rowsShown;
        rows.css('opacity', '0.0').hide().slice(startItem, endItem)
            .css('display', 'table-row').animate({opacity:1}, 30, 0);
    });

});