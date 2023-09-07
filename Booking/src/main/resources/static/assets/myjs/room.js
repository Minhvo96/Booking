const roomForm = document.getElementById('roomForm');
const eCheckBoxCategories = document.getElementsByName('categories');
const tBody = document.getElementById("tBody");
const eSelectType = document.getElementById('type');
const ePagination = document.getElementById('pagination')
const eSearch = document.getElementById('search');
const eModalTitle = document.getElementById("staticBackdropLabel");
const submitBtn = document.getElementById("submit-btn");
// const eOptionsType = eSelectType.querySelectorAll("option");
const formBody = document.getElementById('formBody');

let roomSelected = {};
let roomDetail;
let pageable = {
    page: 1,
    sort: 'id,desc',
    search: ''
}
let categories;
let types;
let rooms = [];

roomForm.onsubmit = async (e) => {
    e.preventDefault();
    let data = getDataFromForm(roomForm);
    data = {
        ...data,
        type: {
            id: data.type
        },
        idCategories: Array.from(eCheckBoxCategories)
            .filter(e => e.checked)
            .map(e => e.value),
        id: roomSelected.id
    }

    let message = "Created"
    if (roomSelected.id) {
        await editRoom(data);
        message = "Edited"
    } else {
        await createRoom(data)
    }

    alert(message);
    await renderTable();
    $('#staticBackdrop').modal('hide');

}

function getDataFromForm(form) {
    // event.preventDefault()
    const data = new FormData(form);
    return Object.fromEntries(data.entries())
}
async function getCategoriesSelectOption() {
    const res = await fetch('api/categories');
    return await res.json();
}
async function getTypesSelectOption() {
    const res = await fetch('api/types');
    return await res.json();
}
async function getList() {
    const response = await fetch(`/api/rooms?page=${pageable.page - 1 || 0}&search=${pageable.search || ''}`);
    //response có status ok hoặc không, header và body
    //{page: 1, size: 10, content: []}
    //{ size: 15, content: [1,2,3]}
    //{page:1 , size: 15, content: [1,2,3]}

    if (!response.ok) {
        // Xử lý trường hợp không thành công ở đây, ví dụ: throw một lỗi hoặc trả về một giá trị mặc định
        throw new Error("Failed to fetch data");
    }

    const result = await response.json();
    pageable = {
        ...pageable,
        ...result
    };
    genderPagination();
    renderTBody(result.content);
    return result; // Trả về kết quả mà bạn đã lấy từ response.json()
    // addEventEditAndDelete();
}

function renderTBody(items) {
    let str = '';
    items.forEach(e => {
        str += renderItemStr(e);
    })
    tBody.innerHTML = str;
}

const genderPagination = () => {
    ePagination.innerHTML = '';
    let str = '';
    //generate preview truoc
    str += `<li class="page-item ${pageable.first ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
            </li>`
    //generate 1234

    for (let i = 1; i <= pageable.totalPages; i++) {
        str += ` <li class="page-item ${(pageable.page) === i ? 'active' : ''}" aria-current="page">
      <a class="page-link" href="#">${i}</a>
    </li>`
    }
    //
    //generate next truoc
    str += `<li class="page-item ${pageable.last ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Next</a>
            </li>`
    //generate 1234
    ePagination.innerHTML = str;

    const ePages = ePagination.querySelectorAll('li'); // lấy hết li mà con của ePagination
    const ePrevious = ePages[0];
    const eNext = ePages[ePages.length-1]

    ePrevious.onclick = () => {
        if(pageable.page === 1){
            return;
        }
        pageable.page -= 1;
        getList();
    }
    eNext.onclick = () => {
        if(pageable.page === pageable.totalPages){
            return;
        }
        pageable.page += 1;
        getList();
    }
    for (let i = 1; i < ePages.length - 1; i++) {
        if(i === pageable.page){
            continue;
        }
        ePages[i].onclick = () => {
            pageable.page = i;
            getList();
        }
    }
}

const onSearch = (e) => {
    e.preventDefault()
    pageable.search = eSearch.value;
    pageable.page = 1;
    getList();
}

function renderItemStr(item) {
    return `<tr>
                    <td>
                        ${item.id}
                    </td>
                    <td title="${item.description}">
                        ${item.name}
                    </td>
                    <td>
                        ${item.price}
                    </td>
                    <td>
                        ${item.type}
                    </td>
                    <td>
                        ${item.roomCategory}
                    </td>
                    <td>
                         <div class="dropdown">
                             <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                             <i class="bx bx-dots-vertical-rounded"></i>
                            </button>
                        <div class="dropdown-menu">
                        <button class="dropdown-item" onclick="showEdit(${item.id})"
                        data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                        ><i class="bx bx-edit-alt me-1"></i> Edit</button
                            >
                        <button class="dropdown-item" onclick="deleteRoom(${item.id})"
                        ><i class="bx bx-trash me-1"></i> Delete</button
                        >
              </div>
            </div>
                    </td>
                </tr>`
}

window.onload = async () => {
    categories = await getCategoriesSelectOption();
    types = await getTypesSelectOption();
    await renderTable()

    renderForm(formBody, getDataInput());
}
function getDataInput() {
    return [
        {
            label: 'Name',
            name: 'name',
            value: roomSelected.name,
            required: true,
            pattern: "^[A-Za-z ]{6,20}",
            message: "Username must have minimum is 6 characters and maximum is 20 characters",
        },
        {
            label: 'Type',
            name: 'type',
            value: roomSelected.typeId,
            type: 'select',
            required: true,
            options: types,
            message: 'Please choose Type'
        },
        {
            label: 'Price',
            name: 'price',
            value: roomSelected.price,
            pattern: "[1-9][0-9]{1,10}",
            message: 'Price errors',
            required: true
        },
        {
            label: 'Description',
            name: 'description',
            value: roomSelected.description,
            pattern: "^[A-Za-z ]{6,120}",
            message: "Description must have minimum is 6 characters and maximum is 20 characters",
            required: true
        },

    ];
}
async function renderTable() {
    const pageable = await getList();
    rooms = pageable.content;
    renderTBody(rooms);
    addEventEditAndDelete();
}
const findById = async (id) => {
    const response = await fetch('/api/rooms/' + id);
    return await response.json();
}
function showCreate() {
    $('#staticBackdropLabel').text('Create Room');
    clearForm();
    renderForm(formBody, getDataInput())
}
async function showEdit(id) {
    $('#staticBackdropLabel').text('Edit Room');
    clearForm();
    roomSelected = await findById(id);
    roomSelected.categoryIds.forEach(idCategory => {
        for (let i = 0; i < eCheckBoxCategories.length; i++) {
            if (idCategory === +eCheckBoxCategories[i].value) {
                eCheckBoxCategories[i].checked = true;
            }
        }
    })
    renderForm(formBody, getDataInput());
}
function clearForm() {
    roomForm.reset();
    roomSelected = {};
}
async function editRoom(data) {
    const res = await fetch('/api/rooms/' + data.id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}
async function deleteRoom(id) {
    const res = await fetch('/api/rooms/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(id)
    });
    if (res.ok) {
        alert("Deleted");
        await getList();
    } else {
        alert("Something went wrong!")
    }
}
async function createRoom(data) {
    const res = await fetch('/api/rooms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}
const addEventEditAndDelete = () => {
    const eEdits = tBody.querySelectorAll('.edit');
    const eDeletes = tBody.querySelectorAll('.delete');
    for (let i = 0; i < eEdits.length; i++) {
        console.log(eEdits[i].id)
        eEdits[i].addEventListener('click', () => {
            showEdit(eEdits[i].dataset.id);
        })
    }
}

