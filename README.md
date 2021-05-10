# Not Another Bookclub
Not Another Bookclub is, at its most basic, a social network built around a love of books and reading. Visitors can create an account, record books that they've read, are currently reading, or plan to read, find other users and bookclubs that are reading the same books, and also peruse bookclub listings, seeing how often they meet, what book they are currently reading, and discussion posts and comments posted by bookclub members. If they're a good match, users can request to join bookclubs, or create their own and invite like-minded friends. NAB is a fully functional, end-to-end CRUD full-stack website, built with Java, SpringBoot, Thymeleaf, jQuery, Bootstrap, MySQL, Javascript, CSS, and HTML using Google APIs and internal RESTful endpoints.

- Bootstrap and Custom CSS
- Spring Boot 2.1.x
- Hibernate ORM + JPA
- Thymeleaf
- MySQL

### Libraries and Utilities Applied

- jQuery [link](https://jquery.com/)
- GoogleBooks API

### First steps

1. Clone this repo locally.
2. Create an `application.properties` file with valid credentials for a MySQL connection, use the `application.properties.example` as a template.
3. Google Books API does *not* require a key, so this should work out of the box for you.

### Some General Examples

In this project you will find some examples for:
- Several complex JPA `joinTable` relationship where users can send requests to join a bookclub or be invited, and well as adding books to reading lists for users and bookclubs. See [here](https://github.com/Not-Another-Bookclub/not-another-bookclub-site/blob/main/src/main/java/com/codeup/springboot_blog/models/BookclubMembership.java)
  [here](https://github.com/Not-Another-Bookclub/not-another-bookclub-site/blob/main/src/main/java/com/codeup/springboot_blog/models/UserBook.java), 
  and [here](https://github.com/Not-Another-Bookclub/not-another-bookclub-site/blob/main/src/main/java/com/codeup/springboot_blog/models/BookclubBook.java).
- A small `Enum` class that contains a list of bookclub membership statuses and a list of 'location types. See model [here](https://github.com/Not-Another-Bookclub/not-another-bookclub-site/blob/main/src/main/java/com/codeup/springboot_blog/models/BookclubMembershipStatus.java) and [here](https://github.com/Not-Another-Bookclub/not-another-bookclub-site/blob/main/src/main/java/com/codeup/springboot_blog/models/Location.java)