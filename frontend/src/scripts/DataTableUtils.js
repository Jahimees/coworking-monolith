import DataTable from 'datatables.net-dt';


export default {
    destroyDataTable,
    initDataTable,
    reloadTable
}

function reloadTable(tableName) {
    destroyDataTable(tableName)
    initDataTable()
}

function destroyDataTable(tableName) {
    if (!DataTable.isDataTable('#' + tableName)) {
        let $dataTable = new DataTable('#' + tableName + "_table")
        $dataTable.data().clear();
        $dataTable.destroy();
    }
}

function initDataTable(tableId, columnDefs) {
    const tableName = tableId ? tableId : "default";
    new DataTable('#' + tableName + "_table", {
        columns: columnDefs,
        order: [[1, 'asc'], [2, 'asc']],
        "language": {
            "decimal": "",
            "emptyTable": "Информации не найдено",
            "info": "Показана страница _PAGE_ из _PAGES_",
            "infoEmpty": "Данные не найдены",
            "infoFiltered": "(отфильтровано из _MAX_ всех возможных данных)",
            "infoPostFix": "",
            "thousands": ",",
            "lengthMenu": "Показывать _MENU_ на одной странице",
            "loadingRecords": "Загрузка...",
            "processing": "",
            "search": "Поиск:",
            "zeroRecords": "Совпадений не найдено",
            "paginate": {
                "first": "<<",
                "last": ">>",
                "next": ">",
                "previous": "<"
            },
            "aria": {
                "sortAscending": ": активировать сортировку по возрастанию",
                "sortDescending": ": активировать сортировку по убыванию"
            }
        }
    })

}
