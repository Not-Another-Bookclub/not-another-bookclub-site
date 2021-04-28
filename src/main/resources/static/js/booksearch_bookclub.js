function bookRender(data){
    for(i = 0; i < data.items.length; i++){
        let thumbnail
        let author
        let path = [[${path}]]
        let csrf = [[${_csrf.token}]]
        let csrfname = [[${_csrf.parameterName}]]
        let bookclubid = [[${bookclubid}]]
        if (!data.items[i].volumeInfo.hasOwnProperty('imageLinks')) {thumbnail = "http://localhost:8080/img/logo-small.png"}
        else {thumbnail = data.items[i].volumeInfo.imageLinks.thumbnail}
        console.log(thumbnail);
        if (!data.items[i].volumeInfo.hasOwnProperty('authors')) {author = "Unknown or not listed"}
        else {author = data.items[i].volumeInfo.authors[0]}
        console.log(thumbnail);

        var html = '<div class="card col-lg-2 col-md-3 col-sm-4 col-6 text-center">'
        // html += "<img src='https://books.google.com/books/content?id=" + data.items[i].id + "&printsec=frontcover&img' alt='Book Cover'>"
        html +=  "<div class='card-top'><img src='" + thumbnail + "' alt='Book Cover'>"
        html += "<div class='card-body'>"
        html += "<h5 class='card-title'>" + data.items[i].volumeInfo.title + " - " + author + "</h5></div></div>"

        html += "<div><form action='/book/add' method='post'> <input hidden value=" + data.items[i].id +" name='book'><input hidden value='" + path + "' name='path'>"
        html += '<input hidden value="' + csrf + '" name="' + csrfname + '"> <input hidden value="'+ bookclubid + '" name="bookclubid"><button class="btn btn-primary">Add to reading list</button></form></div>'


        results.innerHTML+=html;


    }

}



function bookSearch(){
    var searchTitle = document.getElementById('search title').value
    var searchAuthor = document.getElementById('search Author').value
    document.getElementById('results').innerHTML =""



    if (searchAuthor.length <= 0) {
        // if (searchAuthor.length > 0 && searchTitle.length > 0) {
        $.ajax({
            url: "https://www.googleapis.com/books/v1/volumes?q=" + searchTitle
                + "&printType=books",
            dataType:"json",
            success: function (data) {bookRender(data)},
            type: 'GET'
        })}

    else if (searchTitle.length <= 0) {
        $.ajax({
            url: "https://www.googleapis.com/books/v1/volumes?q=+inauthor:" + searchAuthor + "&printType=books",
            dataType: "json",
            success: function (data) {bookRender(data)},
            type: 'GET'
        })}

    else if (searchTitle.length > 0 && searchAuthor.length > 0) {
        $.ajax({
            url: "https://www.googleapis.com/books/v1/volumes?q=" + searchTitle
                + "+inauthor:" + searchAuthor + "&printType=books",
            dataType: "json",
            success: function (data) {bookRender(data)},
            type:'GET'
        })}
}

document.getElementById('button').addEventListener('click', bookSearch, false);
document.getElementById('search Author').addEventListener('keyup', function (event){
    if (event.keyCode === 13) {document.getElementById('button').click()}})
document.getElementById('search title').addEventListener('keyup', function (event){
    if (event.keyCode === 13) {document.getElementById('button').click()}})



