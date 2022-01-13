function showTab(n) {
    let tabs = document.getElementsByClassName("tab");
    tabs[n].style.display = "block";

    if (n === 0) {
        document.getElementById("prevBtn").style.display = "none";
    } else {
        document.getElementById("prevBtn").style.display = "inline";
    }

    if (n === (tabs.length - 1)) {
        document.getElementById("nextBtn").innerHTML = "Отправить";
    } else {
        document.getElementById("nextBtn").innerHTML = "Вперед";
    }

    fixStepIndicator(n);

}

function fixStepIndicator(n) {
    let tabs = document.getElementsByClassName("step");
    for (let i = 0; i < tabs.length; i++) {
        tabs[i].className = tabs[i].className.replace(" active", "");
    }
    tabs[n].className += " active";
}


function swipeNext(valid, currentTab, form) {
    let tabs = document.querySelectorAll('.tab');
    if (!valid) return currentTab;

    tabs[currentTab].style.display = "none";
    currentTab++;
    if (currentTab >= tabs.length) {
        form.submit();
    }
    showTab(currentTab);
    return currentTab;
}

function swipePrev(currentTab) {
    let tabs = document.getElementsByClassName("tab");
    tabs[currentTab].style.display = "none";
    currentTab--;
    showTab(currentTab);
    return currentTab;
}