let pages = {
    current_page: 0,
    records_per_page: 0,
    total: null,

    read(){
        this.current_page = 1;
        this.records_per_page = 20;
        this.total = document.getElementById("list").rows.length;
    }

}