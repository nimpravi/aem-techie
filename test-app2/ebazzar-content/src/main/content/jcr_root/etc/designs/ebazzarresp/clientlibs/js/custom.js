/*** 1.common Menu Drop down  starts****/

$(document).ready(function () {
    $('.parentli').click(function () {
        window.location.href = $(this).children().attr('href');
    });

    $('.customDropdown').click(function () {
        $('.dropDownOption').toggle();
    });

    $('.dropDownOption').mouseleave(function () {
        $('.dropDownOption').hide();
    })
   });   