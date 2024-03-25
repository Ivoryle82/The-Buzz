// Prevent compiler errors when using jQuery. "$" will be given a type of
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error

var $: any;

// The 'this' keyword does not behave in JavaScript/TypeScript like it does in java
// Since there is only one NewEntryForm, we will save it to a global, so that we can
// reference it from methods of the NewEntryForm in situations where 'this' won't 
// normally work
//NOTE: THIS IS THE SINGLETON PATTERN I BELIEVE
var newEntryForm: NewEntryForm;

// // This constant indicates the path to our backend server (hosted on dokku)
// const backendUrl = "https://2024sp-tutorial-exh226.dokku.cse.lehigh.edu";

/**
 * NewEntryForm encapsulated all of the code for the form for adding an entry
 */
class NewEntryForm {
    /**
     * To initialize the object, we say that method of NewEntryForm should
     * run in response to each of the form's buttons being clicked
     */
    constructor() {
        document.getElementById("addCancel")?.addEventListener("click", (e) => {newEntryForm.clearForm();});
        document.getElementById("addButton")?.addEventListener("click", (e) => {newEntryForm.submitForm();});
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        (<HTMLInputElement>document.getElementById("newTitle")).value = "";
        (<HTMLInputElement>document.getElementById("newMessage")).value = "";
        // reset the UI
        (<HTMLElement>document.getElementById("editElement")).style.display = "none";
        (<HTMLElement>document.getElementById("addElement")).style.display = "none";
        (<HTMLElement>document.getElementById("showElements")).style.display = "block";
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitForm() {
        window.alert("Submit form called");
        // get the values of teh two fields, force them to be strings, and check
        // that neither field is empty
        let title = "" + (<HTMLInputElement>document.getElementById("newTitle")).value;
        let msg = "" + (<HTMLInputElement>document.getElementById("newMessage")).value;
        if(title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }

        //set up AJAX
        //Do a POST. When the server replies, the result will go to onSubmitResponse
        const doAjax = async () => {
            await fetch(`/messages`, {
                method: 'POST',
                body: JSON.stringify({
                    mTitle: title,
                    mMessage: msg
                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then( (response) => {
                //If we get an "ok" message return the json
                if(response.ok) {
                    return Promise.resolve(response.json());
                } else { //Otherwise, handle server errors with a detailed popup message
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then( (data) => {
                newEntryForm.onSubmitResponse(data);
                console.log(data);
            }).catch( (error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
        
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a result
     * 
     * @param data : The object returned by the server
     */
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form
        if(data.mStatus === "ok") {
            newEntryForm.clearForm();
        } else if(data.mStatus === "error") { //Handle explicit errors with a detailed popup message
            window.alert("The server replied with an error:\n" + data.mMessage);
        } else { //Handle other errors with a less-detailed popup message
            window.alert("Unspecified error");
        }
    }
} //end class NewEntryForm

// a global for the main ElementList of the program. See newEntryForm for explaination
var mainList: ElementList;

/**
 * ElementList provides a way of seeing all of the data stored on the server
 */
class ElementList {
    /**
     * refresh is the public method for updating messageList
     */
    refresh() {
        // Issue an AJAX GET and then pass the result to update().
        const doAjax = async () => {
            await fetch(`/messages`, {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then( (response) => {
                // If we get an "ok" message, clear the form
                if(response.ok) {
                    return Promise.resolve(response.json());
                } else { //Otherwise, handle server errors with a detailed popup message
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then( (data) => {
                mainList.update(data);
                console.log(data);
            }).catch( (error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }
        //Make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }

    private update(data: any) {
        let elem_messageList = document.getElementById("messageList");

        if(elem_messageList !== null) {
            elem_messageList.innerHTML = "";

            let fragment = document.createDocumentFragment();
            let table = document.createElement('table');

            for(let i = 0; i< data.mData.length; i++) {
                let tr = document.createElement('tr');
                let td_title = document.createElement('td');
                let td_id = document.createElement('td');
                td_title.innerHTML = data.mData[i].mTitle;
                td_id.innerHTML = data.mData[i].mId;
                tr.appendChild(td_id);
                tr.appendChild(td_title);
                tr.appendChild(this.buttons(data.mData[i].mId));
                table.appendChild(tr);
            }
            fragment.appendChild(table);

            elem_messageList.appendChild(fragment);
        }
        
        //mainList.refresh();
        // Find all of the delete buttons, and set their behavior
        const all_delbtns = (<HTMLCollectionOf<HTMLInputElement>>document.getElementsByClassName("delbtn"));
        for(let i = 0; i < all_delbtns.length; i++) {
            all_delbtns[i].addEventListener("click", (e) => {mainList.clickDelete(e);});
        }

        const all_editbtns = (<HTMLCollectionOf<HTMLInputElement>>document.getElementsByClassName("editbtn"));
        for(let i = 0; i < all_editbtns.length; i++) {
            all_editbtns[i].addEventListener("click", (e) => {mainList.clickEdit(e);});
        }

        //mainList.refresh(); for some reason this causes an infinite loop
    }

    /**
     * buttons() adds a 'delete' button to the HTML for each row
     * also adds an 'edit' button to HTML for each row
     */
    private buttons(id: string): DocumentFragment {
        let fragment = document.createDocumentFragment();
        let td = document.createElement('td');

        //create edit button, add to new td, and td to returned fragment
        let btn = document.createElement('button');

        btn.classList.add("editbtn");
        btn.setAttribute('data-value', id);
        btn.innerHTML = 'Edit';
        td.appendChild(btn);
        fragment.appendChild(td);

        //create delete button, add to new td, and td to returned fragment
        td = document.createElement('td');
        btn = document.createElement('button');
        btn.classList.add("delbtn");
        btn.setAttribute('data-value', id);
        btn.innerHTML = 'Delete';
        td.appendChild(btn);
        fragment.appendChild(td);

        return fragment;
    }

    /**
     * clickDelete is the code we run in response to a click of a delete button
     */
    private clickDelete(e: Event) {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked
        const id = (<HTMLElement>e.target).getAttribute("data-value");
        //window.alert(id);

        //Issue an AJAX DELETE and then invoke refresh()
        const doAjax = async () => {
            await fetch(`/messages/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then( (response) => {
                if(response.ok) {
                    return Promise.resolve(response.json());
                } else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then( (data) => {
                mainList.refresh();
                console.log(data);
            }).catch( (error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        //Make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);

        //TODO: we've repeated the same pattern 3+ times now, so think about refactoring the
        //code to maybe use a factory design pattern or something reuseable
    }

    /**
     * clickEdit is the code we run in response to a click of a delete button
     */
    private clickEdit(e: Event) {
        // as in clickDelete, we need the ID of the row
        const id = (<HTMLElement>e.target).getAttribute("data-value");

        // Issue an AJAX GET and then pass the result to editEntryForm.init()
        const doAjax = async () => {
            await fetch(`/messages/${id}`, {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then( (response) => {
                if(response.ok) {
                    return Promise.resolve(response.json());
                } else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then( (data) => {
                editEntryForm.init(data);
                console.log(data);
            }).catch( (error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }
        //make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }
} //end class ElementList

// a global for the EditEntryForm of the program. See newEntryForm for explaination
var editEntryForm: EditEntryForm;

/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 */
class EditEntryForm {
    /**
     * To initialize the object, we say what method of EditEntryForm should
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        document.getElementById("editCancel")?.addEventListener("click", (e) => {editEntryForm.clearForm();});
        document.getElementById("editButton")?.addEventListener("click", (e) => {editEntryForm.submitForm();});
    }

    /**
     * init() is called from an AJAX GET, and should populate the form if and
     * only if the GET did not have an error
     */
    init(data: any) {
        //If we get an "ok" message, fill in the edit form
        if(data.mStatus === "ok") {
            (<HTMLInputElement>document.getElementById("editTitle")).value = data.mData.mTitle;
            (<HTMLInputElement>document.getElementById("editMessage")).value = data.mData.mContent;
            (<HTMLInputElement>document.getElementById("editId")).value = data.mData.mId;
            (<HTMLInputElement>document.getElementById("editCreated")).value = data.mData.mCreated;
            // show the edit form
            (<HTMLElement>document.getElementById("editElement")).style.display = "block";
            (<HTMLElement>document.getElementById("addElement")).style.display = "none";
            (<HTMLElement>document.getElementById("showElements")).style.display = "none";
        } else if(data.mStatus === "error") { //handle explicit errors with a detailed popup message
            window.alert("The server replied with an error:\n" + data.mMessage);
        } else { // Handle other errors with a less-detailed popup message
            window.alert("Unspecified error");
        }
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        (<HTMLInputElement>document.getElementById("editTitle")).value = "";
        (<HTMLInputElement>document.getElementById("editMessage")).value = "";
        (<HTMLInputElement>document.getElementById("editId")).value = "";
        (<HTMLInputElement>document.getElementById("editCreated")).value = "";
        // reset the UI
        (<HTMLElement>document.getElementById("editElement")).style.display = "none";
        (<HTMLElement>document.getElementById("addElement")).style.display = "none";
        (<HTMLElement>document.getElementById("showElements")).style.display = "block";
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call
     */
    submitForm() {
        window.alert("Submit edit form called.");
        //get the calues of the two fields, force them to be strings, and
        //check that neither is empty
        let title = "" + (<HTMLInputElement>document.getElementById("editTitle")).value;
        let msg = "" + (<HTMLInputElement>document.getElementById("editMessage")).value;

        //NB: we assume that the user didn't modify the value of editId
        let id = "" + (<HTMLInputElement>document.getElementById("editId")).value;
        if(title === "" || msg === "" || id === "") {
            window.alert("Error: title, message, or id is not valid");
            return;
        }

        // set up an AJAX PUT.
        // When the server replies, the result will go to onSubmitResponse
        const doAjax = async () => {
            await fetch(`/messages/${id}`, {
                method: 'PUT',
                body: JSON.stringify({
                    mTitle: title,
                    mMessage: msg
                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then( (response) => {
                //If we get an "ok" message, return the json
                if(response.ok) {
                    //return response.json();'
                    return Promise.resolve(response.json());
                } else { //otherwise handle server errors with a detailed popup
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                //return response;
                return Promise.reject(response);
            }).then( (data) => {
                editEntryForm.onSubmitResponse(data);
                console.log(data);
            }).catch( (error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }
        //make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a result
     * 
     * @param data : The object returned by the server
     */
    private onSubmitResponse(data: any) {
        //If we get an "ok" message, clear the form and refresh the main
        //listing of messages
        if(data.mStatus === "ok") {
            editEntryForm.clearForm();
            mainList.refresh();
        } else if(data.mStatus === "error") { //handle explicit errors with a detailed popup message
            window.alert("The server replied with an error:\n" + data.mMessage);
        } else { //handle other errors with a less-detailed popup message
            window.alert("Unspecified error");
        }
    }
}//end class EditEntryForm

//Run some configuration code when the web page loads
document.addEventListener('DOMContentLoaded', () => {
    
    // Create the object that controls teh "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object for the main data list, and populate it with data from the server
    mainList = new ElementList();
    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();

    // set up initial UI state
    (<HTMLElement>document.getElementById("editElement")).style.display = "none";
    (<HTMLElement>document.getElementById("addElement")).style.display = "none";
    (<HTMLElement>document.getElementById("showElements")).style.display = "block";

    // set up the "Add Message" button
    document.getElementById("showFormButton")?.addEventListener("click", (e) => {
        (<HTMLElement>document.getElementById("addElement")).style.display = "block";
        (<HTMLElement>document.getElementById("showElements")).style.display = "none";
    })

    mainList.refresh();
    window.alert('DOMContentLoaded');
}, false);