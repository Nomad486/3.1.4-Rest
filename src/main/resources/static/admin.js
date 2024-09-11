document.addEventListener("DOMContentLoaded", function () {
    fetchCurrentUser(); // Загружаем информацию о текущем пользователе
    fetchUsers(); // Загружаем пользователей при загрузке страницы

    // Обработка создания нового пользователя
    document.getElementById("newUserForm").addEventListener("submit", function (event) {
        event.preventDefault();
        createUser();
    });

    // Обработка обновления пользователя
    document.getElementById("editUserForm").addEventListener("submit", function (event) {
        event.preventDefault();
        updateUser();
    });

    // Обработка удаления пользователя
    document.getElementById("deleteUserForm").addEventListener("submit", function (event) {
        event.preventDefault();
        deleteUser();
    });
});

function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]').getAttribute('content');
}

function getCsrfHeader() {
    return document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
}

function getCsrfHeaders() {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();
    return {
        [csrfHeader]: csrfToken
    };
}

function fetchCurrentUser() {
    fetch("/api/admin/currentUser")
        .then(response => response.json())
        .then(user => {
            // Отображение информации в верхнем меню
            document.querySelector('.navbar-brand').textContent = user.email + ' with roles: ' + user.roles.map(role => role.name).join(', ');

            // Заполняем таблицу с информацией о текущем пользователе во вкладке User
            document.getElementById("currentUserId").textContent = user.id;
            document.getElementById("currentUserFirstName").textContent = user.firstName;
            document.getElementById("currentUserLastName").textContent = user.lastName;
            document.getElementById("currentUserAge").textContent = user.age;
            document.getElementById("currentUserEmail").textContent = user.email;
            document.getElementById("currentUserRoles").textContent = user.roles.map(role => role.name).join(', ');
        })
        .catch(error => console.error('Error fetching current user:', error));
}

function fetchUsers() {
    fetch("/api/admin/users")
        .then(response => response.json())
        .then(users => {
            let usersHtml = users.map(user => `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(role => role.name).join(', ')}</td>
                    <td>
                        <button class="btn btn-primary btn-sm" onclick="openEditModal(${user.id})">Edit</button>
                    </td>
                    <td>
                        <button class="btn btn-danger btn-sm" onclick="openDeleteModal(${user.id})">Delete</button>
                    </td>
                </tr>
            `).join('');
            document.getElementById('usersTable').innerHTML = `
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Age</th>
                            <th>Email</th>
                            <th>Roles</th>
                            <th>Edit</th>
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody>${usersHtml}</tbody>
                </table>
            `;
        })
        .catch(error => console.error('Error fetching users:', error));
}

function loadRolesForNewUser() {
    fetch("/api/admin/roles")
        .then(response => response.json())
        .then(allRoles => {
            const rolesSelect = document.getElementById("roles");
            rolesSelect.innerHTML = ''; // Очищаем текущие значения

            allRoles.forEach(role => {
                let option = document.createElement('option');
                option.value = role.id;
                option.text = role.name;
                rolesSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching roles for new user:', error));
}

document.addEventListener("DOMContentLoaded", function () {
    loadRolesForNewUser();  // Загрузка ролей при открытии формы нового пользователя
});

function createUser() {
    const user = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        age: document.getElementById("age").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        roles: Array.from(document.getElementById("roles").selectedOptions).map(option => ({
            id: option.value
        }))
    };

    fetch("/api/admin/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            ...getCsrfHeaders()  // Добавляем CSRF-заголовки
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error creating user");
            }
            document.getElementById("newUserForm").reset();  // Очищаем форму

            // Переключаемся обратно на вкладку с таблицей пользователей
            const adminTab = document.getElementById("v-pills-admin-tab");
            const usersTableContent = document.getElementById("users-table-content");

            // Активируем вкладку Admin
            adminTab.classList.add("active");
            adminTab.setAttribute("aria-selected", "true");

            // Убираем активный класс с вкладки New User
            const newUserTab = document.querySelector("#v-pills-tab .nav-link.active");
            newUserTab.classList.remove("active");
            newUserTab.setAttribute("aria-selected", "false");

            // Показываем контент таблицы пользователей и скрываем контент New User
            usersTableContent.classList.add("show", "active");
            document.getElementById("new-user-content").classList.remove("show", "active");

            // Обновляем таблицу с пользователями
            fetchUsers();
        })
        .catch(error => console.error('Error creating user:', error));
}



function openEditModal(userId) {
    fetch(`/api/admin/users/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById("editUserId").value = user.id;
            document.getElementById("editFirstName").value = user.firstName;
            document.getElementById("editLastName").value = user.lastName;
            document.getElementById("editAge").value = user.age;
            document.getElementById("editEmail").value = user.email;

            // Очищаем и заполняем роли для редактирования
            fetch("/api/admin/roles")
                .then(response => response.json())
                .then(allRoles => {
                    const rolesSelect = document.getElementById("editRoles");
                    rolesSelect.innerHTML = '';
                    allRoles.forEach(role => {
                        const option = document.createElement("option");
                        option.value = role.id;
                        option.text = role.name;
                        if (user.roles.some(userRole => userRole.id === role.id)) {
                            option.selected = true;
                        }
                        rolesSelect.appendChild(option);
                    });
                    $('#editUserModal').modal('show');
                })
                .catch(error => console.error('Error fetching roles:', error));
        })
        .catch(error => console.error('Error fetching user data:', error));
}


function updateUser() {
    const userId = document.getElementById("editUserId").value;
    const user = {
        firstName: document.getElementById("editFirstName").value,
        lastName: document.getElementById("editLastName").value,
        age: document.getElementById("editAge").value,
        email: document.getElementById("editEmail").value
    };

    const roleIds = Array.from(document.getElementById("editRoles").selectedOptions).map(option => option.value);

    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    fetch(`/api/admin/users/${userId}?roleIds=${roleIds.join(',')}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken  // Передаем CSRF-токен в заголовок
        },
        body: JSON.stringify(user)
    })
        .then(() => {
            fetchUsers();  // Перезагружаем таблицу с пользователями
            $('#editUserModal').modal('hide');  // Закрываем модальное окно
        })
        .catch(error => console.error('Error updating user:', error));
}

function openDeleteModal(userId) {
    fetch(`/api/admin/users/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById("deleteUserId").value = user.id;
            document.getElementById("deleteFirstName").value = user.firstName;
            document.getElementById("deleteLastName").value = user.lastName;
            document.getElementById("deleteEmail").value = user.email;
            document.getElementById("deleteRoles").value = user.roles.map(role => role.name).join(', ');

            $('#deleteUserModal').modal('show');
        })
        .catch(error => console.error('Error fetching user data for delete:', error));
}

function deleteUser() {
    const userId = document.getElementById("deleteUserId").value;

    fetch(`/api/admin/users/${userId}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            ...getCsrfHeaders()  // Добавляем CSRF-заголовки
        }
    })
        .then(() => {
            fetchUsers();  // Обновляем таблицу с пользователями
            $('#deleteUserModal').modal('hide');  // Закрываем модальное окно
        })
        .catch(error => console.error('Error deleting user:', error));
}



