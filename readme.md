<p align="center">
  <img width="160" style="border-radius: 50%;" src="docs/logo.jpg" alt="logo"/>
</p>
<h3 align="center">EMPLOYEE MANAGEMENT SYSTEM</h3>
<hr>

*Employee management system* offers simple operations to manage records, like, creating, updating, and deleting.
It also offers a wide range of advanced functionalities, like sorting & filtering.
The system has both `cli` and `GUI` version.

---

## Live version
- [Project Instructions](https://amalitech-training.notion.site/Employee-Management-System-1ca6c750c0d5818a9e05f5baa28a9e0b)
- [Video](https://docs.google.com/document/d/1WKfelpY1eYdCyLg3MtnKBWgefOgZPXA1UVdIKDvZNhI/edit?usp=sharing)

## Built with
![Tools](https://skillicons.dev/icons?i=java,idea,git,github)

---

## Getting Started
To ge this program running on your local environment, 
1. First, install `Java 17.+`
2. Install `Java FX` - `@latest`
3. Clone the repository ([link](https://github.com/sntakirutimana72/EmployeeMIS))

## How does it work?
Each application version `cli`/`GUI` has its own `entry point`.

- `cli` entry point is in the `cli` package
  ```Java
    package com.employeemis.cli;
    
    public class Main {}
  ```
- Whereas `GUI`'s is directly in main package
  ```Java
  package com.employeemis;
  
  public class Main extends javafx.application.Application {}
  ```
  
### How To Operate `CLI` app version?
`cli` version offers command line interface functionalities where user is prompted to select an action to be performed.
Given the selection, user may be required to going down the further, while selecting an action after another.

For example:
- `Index page` ~ `When user is not authenticated` - Here user is expected to either `login` or `exit`
  ```shell
  +--------------------------------------------------+
  |      Welcome To Employee Management System       |
  +--------------------------------------------------+
  *** Select option ***
  1. Login
  2. Exit
  >
  ```
- `Dashboard page` ~ `User has been authenticated` - Here user can then start interacting with records.
  ```shell
  +--------------------------------------------------+
  |                    Login Form                    |
  +--------------------------------------------------+
  Enter username:
  > ...
  Enter password:
  > ...
  *** Select option ***
  1. Users
  2. Employees
  3. Departments
  4. Permissions
  5. Logout
  6. Exit
  >
  ```
**NOTE**:
  - `...` ~ allows user to go back one level from anywhere in the application.
  - `exit` ~ allows user to terminate the application from anywhere.
  - `Filtering` & `Sorting` doesn't work on the `cli` version

### How To Operate `GUI` app version?
This version doesn't require `authentication` to operate it. So, once fired up, you get straight to business.
<table>
  <tr>
    <th>Employee View</th>
    <td>
      <img src="docs/employees.png"/>
    </td>
  </tr>
</table>
<table>
  <thead>
    <tr>
      <th>Employee Create Form</th>
      <th>Employee Update Form</th>
    </tr>
  </thead>
  <tbody>
    <td>
      <img src="docs/employee_create_form.png" height="300">
    </td>
    <td>
      <img src="docs/employee_update_form.png" height="300"/>
    </td>
  </tbody>
</table>

---
## Authors

👤 **Steve**
- GitHub: [@sntakirutimana72](https://github.com/sntakirutimana72/)

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

Feel free to check the [issues page](https://github.com/sntakirutimana72/EmployeeMIS/issues/)

## Show your support

Give a ⭐️ if you like this project!

## Acknowledgments

- Devs Communities for great free and resourceful articles.

## 📝 License

This project is [MIT](./LICENSE) licensed.
