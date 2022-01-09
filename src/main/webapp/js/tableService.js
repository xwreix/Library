let current_page = 1;
let records_per_page = 20;
let total = document.getElementById("list").rows.length;

function prevPage() {
    if (current_page > 1) {
        current_page--;
        changePage(current_page);
    }
}


function nextPage() {
    if (current_page < pagesAmount()) {
        current_page++;
        changePage(current_page);
    }
}

function pagesAmount() {
    return Math.ceil((total - 1) / records_per_page);
}

function changePage(page) {
    let btnNext = document.getElementById("btn_next");
    let btnPrev = document.getElementById("btn_prev");
    let table = document.getElementById("list");
    let pageSpan = document.getElementById("page");

    if (page < 1) {
        page = 1;
    } else if (page > pagesAmount()) {
        page = pagesAmount();
    }

    [...table.getElementsByTagName('tr')].forEach((tr) => {
        tr.style.display = "none";
    });

    table.rows[0].style.display = "";

    for (let i = (page - 1) * records_per_page + 1; i < (page * records_per_page) + 1; i++) {
        if (table.rows[i]) {
            table.rows[i].style.display = "";
        }
    }
    pageSpan.innerHTML = "Страница: " + page + "/" + pagesAmount();

    if (page === 1) {
        btnPrev.style.visibility = "hidden";
    } else {
        btnPrev.style.visibility = "visible";
    }

    if (page === pagesAmount()) {
        btnNext.style.visibility = "hidden";
    } else {
        btnNext.style.visibility = "visible";
    }

}

const sort = ({target}) => {
    const order = (target.dataset.order = -(target.dataset.order || -1));
    const index = [...target.parentNode.cells].indexOf(target);
    const collator = new Intl.Collator(['en', 'ru'], {numeric: true});
    const comparator = (index, order) => (a, b) => order * collator.compare(
        a.children[index].innerHTML,
        b.children[index].innerHTML
    );

    for (const tBody of target.closest('table').tBodies)
        tBody.append(...[...tBody.rows].sort(comparator(index, order)));

    for (const cell of target.parentNode.cells)
        cell.classList.toggle('sorted', cell === target);
};


function tableSearch() {
    let btnNext = document.getElementById("btn_next");
    let btnPrev = document.getElementById("btn_prev");
    let pageSpan = document.getElementById("page");

    const phrase = document.getElementById('search');
    const table = document.getElementById('list')
    const reg = new RegExp(phrase.value, 'i');
    let flag = false;

    if (phrase.value === "") {
        btnPrev.style.visibility = "visible";
        btnNext.style.visibility = "visible";
        pageSpan.style.visibility = "visible";
    } else {
        btnPrev.style.visibility = "hidden";
        btnNext.style.visibility = "hidden";
        pageSpan.style.visibility = "hidden";
    }

    for (let i = 1; i < table.rows.length; i++) {
        flag = reg.test(table.rows[i].cells[0].innerHTML);

        if (flag) {
            table.rows[i].style.display = "";
        } else {
            table.rows[i].style.display = "none";
        }
    }
}

window.onload = function () {
    changePage(current_page);

    document.querySelectorAll('.sorted thead').forEach(tableTh =>
        tableTh.addEventListener('click', () => sort(event)));
};