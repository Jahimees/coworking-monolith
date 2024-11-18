import DataTable from 'datatables.net-dt';


export default {
    destroyAndInitDataTable,
    initDataTable
}

function destroyAndInitDataTable(tableName, $dataTable) {
    if (!DataTable.isDataTable('#' + tableName)) {
        $dataTable.data().clear();
        $dataTable.destroy();
    }

    initDataTable(tableName);
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
