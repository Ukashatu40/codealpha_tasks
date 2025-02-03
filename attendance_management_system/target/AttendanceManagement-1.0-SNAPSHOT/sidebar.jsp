<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sidebar Toggle Test</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        nav#sidebar {
            width: 250px;
            height: 100vh;
            background: #343a40;
            color: white;
            position: fixed;
            left: 0;
            top: 0;
            transition: left 0.3s ease;
        }

        nav#sidebar.collapsed {
            left: -250px;
        }

        div#content {
            margin-left: 250px;
            transition: margin-left 0.3s ease;
        }

        div#content.collapsed {
            margin-left: 0;
        }
    </style>
</head>
<body>
    <nav id="sidebar">
        <h3>Sidebar</h3>
    </nav>
    <div id="content">
        <button id="sidebar-toggle">Toggle Sidebar</button>
        <p>Content goes here...</p>
    </div>

    <script>
        document.getElementById("sidebar-toggle").addEventListener("click", function () {
            const sidebar = document.getElementById("sidebar");
            const content = document.getElementById("content");
            sidebar.classList.toggle("collapsed");
            content.classList.toggle("collapsed");
        });
    </script>
</body>
</html>
