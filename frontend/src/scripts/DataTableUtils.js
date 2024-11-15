import DataTable from 'datatables.net-dt';


export default {
    destroyAndInitDataTable,
    initDataTable
}

function destroyAndInitDataTable(tableName, $dataTable) {
    if (!DataTable.isDataTable('#' + tableName)) {
        $dataTable.DataTable().data().clear();
        $dataTable.DataTable().destroy();
    }

    initDataTable(tableName);
}

function initDataTable(tableId) {
    const tableName = tableId ? tableId : "default";
    new DataTable('#' + tableName + "_table", {
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
                "first": "Первая",
                "last": "Последняя",
                "next": ">>",
                "previous": "<<"
            },
            "aria": {
                "sortAscending": ": активировать сортировку по возрастанию",
                "sortDescending": ": активировать сортировку по убыванию"
            }
        }
    })

    if (tableId === "appointment") {
        initFilter();
    }
}
