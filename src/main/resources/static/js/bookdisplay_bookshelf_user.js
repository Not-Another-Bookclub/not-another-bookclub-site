let idsearch = [[${books}]]
    let startdates = [[${startdates}]]
    let startdateshtml = [[${startdateshtml}]]
    let finishdates = [[${finishdates}]]
    let finishdateshtml = [[${finishdateshtml}]]

    let findreplace = {}

    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
    return new bootstrap.Popover(popoverTriggerEl)
})


    function googleIdSearch(idSearch,startDate,finishDate,startDateHTML,finishDateHTML){
// console.log("This function runs");
// let idSearch = document.getElementById('search google_id').value
    let results = document.getElementById('results1')
// console.log("search google_id")

    $.ajax({
    url: "https://www.googleapis.com/books/v1/volumes/" + idSearch,
    dataType: "json",

    success: function (data) {

    let thumbnail
    let author

    // use dom to display ajax rtn

    if (!data.volumeInfo.hasOwnProperty('imageLinks')) {thumbnail = "http://localhost:8080/img/logo-small.png"}
    else if (!data.volumeInfo.imageLinks.hasOwnProperty('thumbnail')) {thumbnail = "http://localhost:8080/img/logo-small.png"}
    else {thumbnail = data.volumeInfo.imageLinks.thumbnail}
    console.log(thumbnail);
    if (!data.volumeInfo.hasOwnProperty('authors')) {author = "Unknown or not listed"}
    else {author = data.volumeInfo.authors[0]}
    if (startDate === null) {startDate = "Not yet started."};
    if (finishDate === null) {finishDate = "Not finished yet."}

    var html = '<div class="card col-md-3 col-sm-4 col-6 text-center">'
    // html += "<img src='https://books.google.com/books/content?id=" + data.items[i].id + "&printsec=frontcover&img' alt='Book Cover'>"
    html += "<img src='" + thumbnail + "' alt='Book Cover'>"
    html += "<div class='card-body'>"
    html += "<h5 class='card-title'>" + data.volumeInfo.title + " - " + data.volumeInfo.authors[0] + "</h5><br>"
    html += "<h6> Started reading: "+ startDate +"</h6>"
    html += "<h6> Finished reading: "+ finishDate +"</h6></div>"
    // html += '<div><a href="#" class="btn btn-primary" id="'+ idSearch +'">Remove from reading list</a>'
    html += '<button href="" class="btn book-selector btn-primary" data-google-i-d="'+ idSearch +'" data-startdate="'+startDate+'" data-startdatehtml="'+ startDateHTML+'" data-finishdate="'+ finishDate +'" data-finishdatehtml="'+ finishDateHTML +'" data-bs-toggle="modal" data-bs-target="#myModal">See details</button></div></div>'
    // html += "<div class='card-bottom1'><p class='card-text'>" + data.volumeInfo.description + "</p></div></div>"

    results.innerHTML += html;
    findreplace[idSearch] = data.volumeInfo.title;

    $('body :not(script)').contents().filter(function () {
    return this.nodeType === 3;
}).replaceWith(function () {
    return this.nodeValue.replace(idSearch, data.volumeInfo.title)})

},


    type: 'Get',
}
    )
}
    if (typeof idsearch == "string") {googleIdSearch(idsearch,startdateshtml,finishdateshtml);}
    // console.log("This book is a string!")
    // $(".card-bottom1").hide()
    // console.log("Firing after the hide from string!")
    // $('.card-top1').click(function() {
    // console.log("I'm firing from the click listener!")
    // let thisCardBottom = $(this).next()
    // // $(this).parent().toggleClass("col-lg-4").toggleClass("col-lg-2");
    // $(".card-bottom1").not(thisCardBottom).slideUp(500)
    // $(this).next().slideToggle(1000)})}
    else {
    for(let i = 0; i < idsearch.length; i++) {
    googleIdSearch(idsearch[i],startdates[i],finishdates[i],startdateshtml[i], finishdateshtml[i]);
}
}
// console.log("These books are a list!")
// $(".card-bottom1").hide()
//     console.log("Firing after the hide from list!")
// $('.card-top1').click(function() {
//     console.log("I'm firing from the click listener!")
//     let thisCardBottom = $(this).next()
//     // $(this).parent().toggleClass("col-lg-4").toggleClass("col-lg-2");
//     $(".card-bottom1").not(thisCardBottom).slideUp(500)
//     $(this).next().next().slideToggle(1000)}

    const title = document.getElementById('myModalLabel');
    const modalImage = document.getElementById('modalImage');
    const body = document.getElementById('modalBody');
    const startdatetext = document.getElementById('startDateText');
    const finishdatetext = document.getElementById('finishDateText');
    const startdatecalendar = document.getElementById('startDate');
    const finishdatecalendar = document.getElementById('finishDate');
    const modalbookid = document.getElementById('modalbookid');



    function modalPopulate(idSearch,start,finish,startHTML,finishHTML){
    $.ajax({
    url: "https://www.googleapis.com/books/v1/volumes/" + idSearch,
    dataType: 'json',
    success: function (data) {
    let thumbnail
    let author

    if (!data.volumeInfo.hasOwnProperty('imageLinks')) {
    thumbnail = "http://localhost:8080/img/logo-small.png"
} else if (!data.volumeInfo.imageLinks.hasOwnProperty('thumbnail')) {
    thumbnail = "http://localhost:8080/img/logo-small.png"
} else {
    thumbnail = data.volumeInfo.imageLinks.thumbnail
}
    console.log(thumbnail);
    if (!data.volumeInfo.hasOwnProperty('authors')) {
    author = "Unknown or not listed"
} else {
    author = data.volumeInfo.authors[0]
}

    title.innerText = data.volumeInfo.title + " - " + author;
    modalImage.innerHTML = '<img src="' + thumbnail + '">'
    body.innerHTML = data.volumeInfo.description;
    startdatetext.innerText = "Currently set to: " + start;
    finishdatetext.innerText = "Currently set to: " + finish;
    startdatecalendar.value = startHTML;
    finishdatecalendar.value = finishHTML;
    modalbookid.value = idSearch;
},

    type: 'Get'

});
}

    setTimeout(function() {
    //This was the original version set to run on a timeout, instead of set to run on the success.
    // if (typeof idsearch == "string")
    // {$('body :not(script)').contents().filter(function () {
    //     return this.nodeType === 3;
    // }).replaceWith(function () {
    //     return this.nodeValue.replace(idsearch, findreplace[idsearch])})
    //
    // }
    // else {
    // for (let i = 0; i < idsearch.length; i++) {
    //     $('body :not(script)').contents().filter(function () {
    //         return this.nodeType === 3;
    //     }).replaceWith(function () {
    //         return this.nodeValue.replace(idsearch[i], findreplace[idsearch[i]]);
    //     })
    // }

    // $("* :contains(idsearch[i])").each(function() {
    //     var replaced = $(this).html().replace(/idsearch[i]/g, findreplace[idsearch[i]])
    //     $(this).html(replaced);
    // })
    // }
    // document.getElementsByClassName('book-selector').click(function (e){
    //     e.preventDefault()
    //     document.getElementById('book').value = this.id;
    // })
    $('.book-selector').click(function() {
    // console.log(this);
    modalPopulate(this.dataset.googleID, this.dataset.startdate, this.dataset.finishdate, this.dataset.startdatehtml, this.dataset.finishdatehtml)
})
}
    , 750)