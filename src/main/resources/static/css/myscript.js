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

            $.get("https://www.googleapis.com/books/v1/volumes?q=" + search, function (response){

               // console.log(response);

               for(i=0; i<response.items.length; i++){
                   // get the ISBN of the book


                    // get the title of the book
                   title=$('<h5 class = "center-align white-text">' + response.items[i].volumeInfo.title + '</h5>');
                   // get the author of the book
                   author=$('<h5 class = "center-align white-text">' + response.items[i].volumeInfo.author + '</h5>');

               }

            });
        }


    });
    return false;

});