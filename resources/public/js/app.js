var Librarian = function() {
  function createButton(params) {
    var button = $('<button>');
    button.addClass('btn').addClass(params.buttonClass);
    if(params.icon) {
      var icon = $('<i>').addClass(params.icon);
      button.append(icon);
      button.append(' ');
    }
    button.append(params.caption);
    button.click(params.callback);
    return button;
  }
    
  function isExistingBook(row) {
    return row.attr('data-id');
  }
    
  function getEditedBook(row) {
    return {_id: row.attr('data-id'),
            author: row.find('input[name="author"]').val().trim(), 
            title: row.find('input[name="title"]').val().trim()};
  }
    
  function validateBook(book) {
    var errorMsg = '';
    if(book.author.length == 0) {
        errorMsg += 'Author is missing\n';
    }
    if(book.title.length == 0) {
        errorMsg += 'Title is missing\n';
    }
    if(errorMsg.length > 0) {
        alert(errorMsg);
    }
    return errorMsg.length == 0;
  }

  function reviewBook(row) {
    return function() { 
      var bookId = row.attr('data-id');
      // FIXME Implement me!
    };
  }

  function deleteBook(row) {
    return function() {
      var bookId = row.attr('data-id');
      $.ajax('/books/' + bookId, {
        type: 'DELETE',
        dataType: 'json',
        success: function(book) {
          row.remove();
        }
      });
    };
  }
    
  function performSave(book, row) {
    $.ajax('/books', {
      type: 'PUT',
      data: book,
      dataType: 'json',
      success: function(book) {
        row.empty();
        appendBookAdmin(row, book);
      }
    });
  }
    
  function performUpdate(book, row) {
    $.ajax('/books/' + book._id, {
      type: 'POST',
      data: book,
      dataType: 'json',
      success: function(book) {
        row.empty();
        appendBookAdmin(row, book);
      }
    });
  }
    
  function updateBook(row) {
    return function() {
      var book = getEditedBook(row);
      if(validateBook(book)) {
        row.find('input').attr('disabled', true);
        row.find('button').attr('disabled', true);
        if(isExistingBook(row)) {
          performUpdate(book, row);
        } else {
          performSave(book, row);
        }
      }
    };
  }
    
  function cancelEdit(row) {
    return function() {
      if(isExistingBook(row)) {
        var book = {
            _id: row.attr('data-id'), 
            author: row.attr('data-author'), 
            title: row.attr('data-title')};
        row.empty();
        appendBookAdmin(row, book);
      } else {
        row.remove();
      }
    };
  }
    
  function editBook(row) {
    return function() {
      row.empty();
      if(isExistingBook(row)) {
        row.append('<td>' + row.attr('data-id') + '</td>');
        row.append('<td><input type="text" name="author" value="' + row.attr('data-author') + '" /></td>');
        row.append('<td><input type="text" name="title" value="' + row.attr('data-title') + '" /></td>');
      } else {
        row.append('<td>');
        row.append('<td><input type="text" name="author" placeholder="Author" /></td>');
        row.append('<td><input type="text" name="title" placeholder="Title" /></td>');
      }
      var saveButton = createButton({
        buttonClass: 'btn btn-primary',
        icon: 'icon-ok icon-white',
        caption: 'Save',
        callback: updateBook(row)});
      
      var cancelButton = createButton({
        buttonClass: 'btn',
        caption: 'Cancel',
        callback: cancelEdit(row)});
      
      var buttons = $('<td>');
        buttons.append(saveButton);
        buttons.append(' ');
        buttons.append(cancelButton);
        row.append(buttons);
        row.find('input:first').focus();
    };
  }
    
  function newBookClicked() {
    var row = $('<tr>');
    $('.book-list').append(row);
    editBook(row).call();
  }
    
  function appendBook(row, book) {
    row.attr('data-id', book._id);
    row.attr('data-author', book.author);
    row.attr('data-title', book.title);
    row.append('<td>' + book._id + '</td>');
    row.append('<td>' + book.author + '</td>');
    row.append('<td>' + book.title + '</td>');
  }

  function appendBookAdmin(row, book) {
    appendBook(row, book);
        
    var editButton = createButton({
      buttonClass: 'btn btn-primary',
      icon: 'icon-edit icon-white',
      caption: 'Edit',
      callback: editBook(row)});
        
    var deleteButton = createButton({
      buttonClass: 'btn btn-danger',
      icon: 'icon-trash icon-white',
      caption: 'Delete',
      callback: deleteBook(row)});

    var reviewButton = createButton({
      buttonClass: 'btn',
      icon: 'icon-star',
      caption: 'Review',
      callback: reviewBook(row)});
        
    var buttons = $('<td>');
    buttons.append(editButton);
    buttons.append(' ');
    buttons.append(reviewButton);
    buttons.append(' ');
    buttons.append(deleteButton);
    row.append(buttons);
  }

  function loginClicked() {
    $('#login-form').lightbox_me({
      centered: true, 
      onLoad: function() { 
        $('#login-form').find('input:first').focus();
      }
    });
    return false;
  }

  function createAccountClicked() {
    $('#signup-form').lightbox_me({
      centered: true, 
      onLoad: function() { 
        $('#signup-form').find('input:first').focus();
      }
    });
    return false;
  }

  function signinClicked() {
    var login = $("#login-form").find('input[name="login"]').val().trim();
    var pw = $("#login-form").find('input[name="password"]').val().trim();
    if(login.length == 0 || pw.length == 0) {
      alert('Please provide user name and password');
    } else {
      $.ajax('/login', {
        type: 'POST',
        data: {"login": login, "password": pw},
        dataType: 'json',
        success: function(data) {
          if(data != null && data != undefined && data.successful) {
            location.reload();
          } else {
            showError($("#login-form"), data.errorDetail);
          }
        }
      });
    }
    return false;
  }

  function signupClicked() {
    var login = $("#signup-form").find('input[name="login"]').val().trim();
    var pw = $("#signup-form").find('input[name="password"]').val().trim();
    var pw2 = $("#signup-form").find('input[name="password2"]').val().trim();

    if(login.length == 0 || pw.length == 0) {
      alert('Please provide user name and password');
    } else if(pw != pw2) {
      alert('Please type the same password in both fields');
    } else {
      $.ajax('/signup', {
        type: 'POST',
        data: {"login": login, "password": pw},
        dataType: 'json',
        success: function(data) {
          if(data != null && data != undefined && data.successful) {
            location.reload();
          } else {
            showError($("#signup-form"), data.errorDetail);
          }
        }
      });
    }
    return false;
  }

  function hideErrors() {
    $("#login-form-container .alert-error").remove();
  }

  function showError(form, message) {
    hideErrors();
    $("<div />").addClass("alert alert-error").html(message).insertAfter(form);
  }

  function logoutClicked() {
    $.ajax('/logout', {
      type: 'POST',
      dataType: 'json',
      success: function(data) {
        location.reload();
      }
    });
    return false;
  }

  function loadBookList(populateRowFn) {
    $.getJSON('/books', function(json) {
      var bookTable = $('.book-list');
      $.each(json, function(key, val) {
        var row = $('<tr>');
        populateRowFn(key, val, row);
        bookTable.append(row);
      });
    });
  }

  function initFront() {
    loadBookList(function (key, val, row) {
        appendBook(row, val);
    });
    $('#btn-login').click(loginClicked);
    $('#btn-create-account').click(createAccountClicked);
    $('#btn-signin').click(signinClicked);
    $('#btn-signup').click(signupClicked);
    $('#btn-logout').click(logoutClicked);
    hideErrors();
    
    $('#login-form').hide();
    $('#signup-form').hide();
  }

  function initAdmin() {
    loadBookList(function (key, val, row) {
      appendBookAdmin(row, val);
    });
    $('#btn-add-book').click(newBookClicked);
    $('#btn-logout').click(logoutClicked);
    $('#login-form .alert').hide();
  }

  return {
    initFront: initFront,
    initAdmin: initAdmin
  };
};
