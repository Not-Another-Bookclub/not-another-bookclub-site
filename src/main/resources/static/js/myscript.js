$(document).ready(function (){

    $("#myform").submit(function (){

        var search = $("#books").val();

        if(search == '')
        {
            alert("Please enter something in the field first");

        }
        else{
            var url ='';
            var img = '';
            var title = '';
            var author = '';

            $.get("https://www.googleapis.com/books/v1/mylibrary/bookshelves?key=AIzaSyChxQmAq5xeCpMDMDm5O7R8FWW50Hi9K38"

            $.get("https://www.googleapis.com/books/v1/volumes?q=" + search, function (response){

               // console.log(response);

               for(i=0; i<response.items.length; i++){
                   // get the ISBN of the book
                   ISBN=$('<h5 class = "center-align black-text">' + response.items[i].volumeInfo.ISBN + '</h5>');
                   // get the title of the book
                   title=$('<h5 class = "center-align black-text">' + response.items[i].volumeInfo.title + '</h5>');
                   // get the author of the book
                   author=$('<h5 class = "center-align black-text">' + response.items[i].volumeInfo.author + '</h5>');
                   // get the image associated with the book
                   img=$('<img class="aligning card z-depth-5" id="dynamic"><br><a href=' + response.items[i].volumeInfo.infoLink + '><button id="imagebutton" class="btn red aligning">Read More</button></a>');

                   url = response.items[i].volumeInfo.imageLinks.thumbnail;
                   // attach the image url
                   img.attr('src', url);


                   ISBN.appendTo(this.#result);
                   title.appendTo(this.#result);
                   author.appendTo(this.#result);
                   img.appendTo(this.#result);


               }

            });
        }


    });
    return false;

});