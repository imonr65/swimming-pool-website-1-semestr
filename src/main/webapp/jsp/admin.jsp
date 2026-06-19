<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Admin Panel</title>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<div class="container">
    <h1>Admin Panel</h1>

    <div class="tabs">
        <div class="tabs">
            <a href="${pageContext.request.contextPath}/admin/products" class="tab-link">Products</a>
            <a href="${pageContext.request.contextPath}/admin/trainers" class="tab-link">Trainers</a>
            <a href="${pageContext.request.contextPath}/admin/workouts" class="tab-link">Workouts</a>
        </div>

    </div>

    <div id="Products" class="tab-content ${activeTab == 'products' ? 'active' : ''}">
        <div class="form-section">
            <h3>Add New Product</h3>
            <div id="productMessage" style="display: none;"></div>

            <form id="productForm">
                <div class="form-group">
                    <label for="productName">Product Name:</label>
                    <input type="text" id="productName" name="productName" required>
                </div>

                <div class="form-group">
                    <label for="categoryId">Category:</label>
                    <select id="categoryId" name="categoryId" required>
                        <option value="">Select Category</option>
                        <c:forEach items="${productCategories}" var="category">
                            <option value="${category.id}">${category.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" required></textarea>
                </div>

                <div class="form-group">
                    <label for="price">Price:</label>
                    <input type="number" id="price" name="price" min="0" required>
                </div>

                <div class="form-group">
                    <label for="productPhoto">Product Photo:</label>
                    <input type="file" id="productPhoto" name="productPhoto" accept="image/*" required>
                    <div id="imagePreview" style="margin-top: 10px; display: none;">
                        <img id="previewImage" src="#" alt="Preview">
                    </div>
                </div>

                <div class="form-group">
                    <label>
                        <input type="checkbox" id="isAvailable" name="isAvailable"> Available
                    </label>
                </div>

                <button type="button" class="btn btn-primary" onclick="addProduct()">Add Product</button>
            </form>
        </div>

        <div class="form-section">
            <h3>Delete Product</h3>
            <div id="deleteProductMessage" style="display: none;"></div>

            <form id="deleteProductForm">
                <div class="form-group">
                    <label for="deleteProductId">Product ID:</label>
                    <input type="number" id="deleteProductId" name="deleteProductId" min="1" required>
                </div>
                <button type="button" class="btn btn-danger" onclick="deleteProduct()">Delete Product</button>
            </form>
        </div>
    </div>

    <div id="Trainers" class="tab-content ${activeTab == 'trainers' ? 'active' : ''}">
        <div class="form-section">
            <h3>Add New Trainer</h3>
            <div id="trainerMessage" style="display: none;"></div>

            <form id="trainerForm">
                <div class="form-group">
                    <label for="trainerName">Trainer Name:</label>
                    <input type="text" id="trainerName" name="trainerName" required>
                </div>

                <div class="form-group">
                    <label for="specialization">Specialization:</label>
                    <input type="text" id="specialization" name="specialization" required>
                </div>

                <div class="form-group">
                    <label for="experience">Experience (years):</label>
                    <input type="number" id="experience" name="experience" min="0" required>
                </div>

                <div class="form-group">
                    <label for="trainerDescription">Description:</label>
                    <textarea id="trainerDescription" name="trainerDescription" rows="3"
                              placeholder="Short description about the trainer"></textarea>
                </div>

                <div class="form-group">
                    <label for="trainerBiography">Biography:</label>
                    <textarea id="trainerBiography" name="trainerBiography" rows="5"
                              placeholder="Detailed biography of the trainer"></textarea>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                </div>

                <button type="button" class="btn btn-primary" onclick="addTrainer()">Add Trainer</button>
            </form>
        </div>

        <div class="form-section">
            <h3>Delete Trainer</h3>
            <div id="deleteTrainerMessage" style="display: none;"></div>

            <form id="deleteTrainerForm">
                <div class="form-group">
                    <label for="deleteTrainerId">Trainer ID:</label>
                    <input type="number" id="deleteTrainerId" name="deleteTrainerId" min="1" required>
                </div>
                <button type="button" class="btn btn-danger" onclick="deleteTrainer()">Delete Trainer</button>
            </form>
        </div>
    </div>

    <div id="Workouts" class="tab-content ${activeTab == 'workouts' ? 'active' : ''}">
        <div class="form-section">
            <h3>Add New Workout</h3>
            <div id="workoutMessage" style="display: none;"></div>

            <form id="workoutForm">
                <div class="form-group">
                    <label for="workoutDescription">Description:</label>
                    <textarea id="workoutDescription" name="description" required
                              placeholder="Enter workout description"></textarea>
                </div>

                <div class="form-group">
                    <label for="workoutPeopleType">People Type:</label>
                    <select id="workoutPeopleType" name="peopleTypeId" required>
                        <option value="">Select People Type</option>
                        <c:forEach items="${peopleTypes}" var="peopleType">
                            <option value="${peopleType.id}">${peopleType.typeName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="workoutCategory">Category:</label>
                    <select id="workoutCategory" name="categoryId" required>
                        <option value="">Select Category</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}">${category.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="workoutLevel">Level:</label>
                    <select id="workoutLevel" name="levelId" required>
                        <option value="">Select Level</option>
                        <c:forEach items="${levels}" var="level">
                            <option value="${level.id}">${level.levelName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>
                        <input type="checkbox" id="workoutHasCoach" name="hasCoach"> Has Coach
                    </label>
                </div>

                <button type="button" class="btn btn-primary" onclick="addWorkout()">Add Workout</button>
            </form>
        </div>

        <div class="form-section">
            <h3>Delete Workout</h3>
            <div id="deleteWorkoutMessage" style="display: none;"></div>

            <form id="deleteWorkoutForm">
                <div class="form-group">
                    <label for="deleteWorkoutId">Workout ID:</label>
                    <input type="number" id="deleteWorkoutId" name="workoutId" min="1" required>
                </div>
                <button type="button" class="btn btn-danger" onclick="deleteWorkout()">Delete Workout</button>
            </form>
        </div>
    </div>
</div>


<script>
    function addProduct() {
        const formData = new FormData();

        formData.append('name', $('#productName').val());
        formData.append('categoryId', $('#categoryId').val());
        formData.append('description', $('#description').val());
        formData.append('price', $('#price').val());
        formData.append('isAvailable', $('#isAvailable').is(':checked'));

        const photoInput = document.getElementById('productPhoto');
        if (photoInput && photoInput.files && photoInput.files[0]) {
            formData.append('photo', photoInput.files[0]);
        }

        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/admin/products",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                showMessage('Product added successfully!', 'success', '#productMessage');
                $('#productForm')[0].reset();
                $('#imagePreview').hide();
            },
            error: function (xhr) {
                showMessage('Error adding product: ' + xhr.responseText, 'error', '#productMessage');
            }
        });
    }

    function deleteProduct() {
        const productId = $('#deleteProductId').val();

        if (!productId) {
            showMessage('Please enter product ID', 'error', '#deleteProductMessage');
            return;
        }

        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/admin/products",
            data: {
                action: "delete",
                id: productId
            },
            success: function (response) {
                showMessage('Product deleted successfully!', 'success', '#deleteProductMessage');
                $('#deleteProductForm')[0].reset();
            },
            error: function (xhr) {
                showMessage('Error deleting product: ' + xhr.responseText, 'error', '#deleteProductMessage');
            }
        });
    }

    function addTrainer() {
        const trainerData = {
            name: $('#trainerName').val(),
            specialization: $('#specialization').val(),
            experience: parseInt($('#experience').val()),
            email: $('#email').val(),
            description: $('#trainerDescription').val(),
            biography: $('#trainerBiography').val()
        };

        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/admin/trainers",
            data: JSON.stringify(trainerData),
            contentType: "application/json",
            success: function(response) {
                showMessage('Trainer added successfully!', 'success', '#trainerMessage');
                $('#trainerForm')[0].reset();
            },
            error: function(xhr) {
                showMessage('Error adding trainer: ' + xhr.responseText, 'error', '#trainerMessage');
            }
        });
    }

    function deleteTrainer() {
        const trainerId = $('#deleteTrainerId').val();

        if (!trainerId) {
            showMessage('Please enter trainer ID', 'error', '#deleteTrainerMessage');
            return;
        }

        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/admin/trainers",
            data: {
                action: "delete",
                id: trainerId
            },
            success: function (response) {
                showMessage('Trainer deleted successfully!', 'success', '#deleteTrainerMessage');
                $('#deleteTrainerForm')[0].reset();
            },
            error: function (xhr) {
                showMessage('Error deleting trainer: ' + xhr.responseText, 'error', '#deleteTrainerMessage');
            }
        });
    }

    function addWorkout() {
        const workoutData = {
            description: $('#workoutDescription').val(),
            peopleTypeId: parseInt($('#workoutPeopleType').val()),
            categoryId: parseInt($('#workoutCategory').val()),
            levelId: parseInt($('#workoutLevel').val()),
            hasCoach: $('#workoutHasCoach').is(':checked')
        };

        if (!workoutData.description || !workoutData.peopleTypeId ||
            !workoutData.categoryId || !workoutData.levelId) {
            showMessage('Please fill all required fields', 'error', '#workoutMessage');
            return;
        }

        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/admin/workouts",
            data: JSON.stringify(workoutData),
            contentType: "application/json",
            success: function (response) {
                showMessage('Workout added successfully!', 'success', '#workoutMessage');
                $('#workoutForm')[0].reset();
                loadWorkouts();
            },
            error: function (xhr) {
                showMessage('Error adding workout: ' + xhr.responseText, 'error', '#workoutMessage');
            }
        });
    }

    function deleteWorkout() {
        const workoutId = $('#deleteWorkoutId').val();

        if (!workoutId) {
            showMessage('Please enter workout ID', 'error', '#deleteWorkoutMessage');
            return;
        }

        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/admin/workouts",
            data: {
                action: "delete",
                id: workoutId
            },
            success: function (response) {
                showMessage('Workout deleted successfully!', 'success', '#deleteWorkoutMessage');
                $('#deleteWorkoutForm')[0].reset();
                loadWorkouts();
            },
            error: function (xhr) {
                showMessage('Error deleting workout: ' + xhr.responseText, 'error', '#deleteWorkoutMessage');
            }
        });
    }

    function loadWorkouts() {
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/admin/workouts",
            success: function (workouts) {
                const container = document.getElementById('workoutsList');
                container.innerHTML = '';

                if (!workouts || workouts.length === 0) {
                    container.innerHTML = '<p class="no-data">No workouts found</p>';
                    return;
                }

                let html = '<div class="workouts-table"><table>';
                html += '<thead><tr><th>ID</th><th>Description</th><th>Type</th><th>Category</th><th>Level</th><th>Has Coach</th></tr></thead>';
                html += '<tbody>';

                workouts.forEach(workout => {
                    html += `
                        <tr>
                            <td>${workout.id}</td>
                            <td>${workout.description}</td>
                            <td>${workout.peopleType.typeName}</td>
                            <td>${workout.category.categoryName}</td>
                            <td>${workout.level.levelName}</td>
                            <td>${workout.hasCoach ? 'Yes' : 'No'}</td>
                        </tr>
                    `;
                });

                html += '</tbody></table></div>';
                container.innerHTML = html;
            },
            error: function (xhr) {
                showMessage('Error loading workouts: ' + xhr.responseText, 'error', '#workoutMessage');
            }
        });
    }

    function showMessage(message, type, container) {
        const messageDiv = $(container);
        messageDiv.removeClass('success-message error-message');

        if (type === 'success') {
            messageDiv.addClass('success-message');
        } else {
            messageDiv.addClass('error-message');
        }

        messageDiv.text(message).show();

        setTimeout(() => {
            messageDiv.hide();
        }, 5000);
    }

    function loadCategories() {
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/admin/categories",
            success: function (categories) {
                const select = $('#categoryId');
                select.empty();
                select.append('<option value="">Select Category</option>');

                categories.forEach(category => {
                    select.append(`<option value="${category.id}">${category.categoryName}</option>`);
                });
            }
        });
    }

    document.getElementById('productPhoto')?.addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById('previewImage').src = e.target.result;
                document.getElementById('imagePreview').style.display = 'block';
            }
            reader.readAsDataURL(file);
        } else {
            document.getElementById('imagePreview').style.display = 'none';
        }
    });

    $(document).ready(function () {
        setActiveTabFromURL();
        loadCategories();
    });
</script>
</body>
</html>