let tab = {
    currentTab: 0,
    tabs: null,

    read() {
        this.tabs = document.querySelectorAll('.tab');
    },

    showTab() {
        let tabs = document.getElementsByClassName("tab");
        tabs[this.currentTab].style.display = "block";

        if (this.currentTab === 0) {
            document.getElementById("prevBtn").style.display = "none";
        } else {
            document.getElementById("prevBtn").style.display = "inline";
        }

        if (this.currentTab === (tabs.length - 1)) {
            document.getElementById("nextBtn").innerHTML = "Отправить";
        } else {
            document.getElementById("nextBtn").innerHTML = "Вперед";
        }

        fixStepIndicator(this.currentTab);
    },

    swipeNext(valid, form) {
        if (!valid) return this.currentTab;

        this.tabs[this.currentTab].style.display = "none";
        this.currentTab++;
        if (this.currentTab >= this.tabs.length) {
            console.log(newIssue.discount.value);
            form.submit();
        }
        this.showTab();
        return this.currentTab;
    },


    swipePrev() {
        let tabs = document.getElementsByClassName("tab");
        tabs[this.currentTab].style.display = "none";
        this.currentTab--;
        this.showTab();
        return this.currentTab;
    },
}

function fixStepIndicator(n) {
    let tabs = document.getElementsByClassName("step");
    for (let i = 0; i < tabs.length; i++) {
        tabs[i].className = tabs[i].className.replace(" active", "");
    }
    tabs[n].className += " active";
}

function activity(issue){
    tab.read();

    tab.showTab();

    issue.read(document);

    issue.setDate();

    issue.form.addEventListener('submit', function (event) {
        event.preventDefault();
    });

    $($(".add")).click(function () {
        issue.addBook();

    });

    $(issue.wrapper).on("click", ".delete", function () {
        $(this).parent('div').remove();
    });

    $($('#prevBtn')).click(function () {
        tab.currentTab = tab.swipePrev();
    });

    $($('#nextBtn')).click(function () {
        if (tab.currentTab === 0) {
            issue.validateReader();
        } else if (tab.currentTab === 1) {
            issue.validateBooks();
        } else {
            issue.form.submit();
        }
    });
}
