/**
 * TodoList is the data model. It defines the primary data structure used by our
 * application
 * 
 * Note that since we are using JavaScript, the language is loosely typed
 * Inspecting the code, we can figure out the data model consists of an
 * array of objects, where each object has three fields
 * 
 *  - id: a unique identifier for this object. Note that it cannot simply be the index of the
 *    element in the array, because we want to be able to delete elements.
 * 
 *  - data: the data associated with this object. This is just text, but since we will
 *    put it into an HTML element, it's legal to put HTML tags in the text. (this can be dangerous)
 * 
 *  - complete : a boolean to indicate if the item is completed or not
 * 
 * Note, that TodoList consists of static fields and a bunch of static methods.
 * This means it is something of a singleton pattern (there is only going to be one TodoList) 
 * 
 * Note: in our actual program stuff like this will be in an SQL database
 */

export class TodoList {
    /** The array of {id, data, complete} tuples */
    static #State = [];

    /**A strictly monotomically increasing counter, for Ids */
    static #IdGenerator = 0;

    /**
     * Add a new item to the Todo List by providing its id, data, and completion status
     */
    static CreateItem() {
        let id = this.#IdGenerator++;
        let complete = false;
        let data = "Task " + id;

        //Note this syntax: we don't have to say {id:id, data:data, complete:complete}
        //Since the variable names for the content of each field match the field names
        //we can simplify the above to {id,data,complete}

        let res = {id, data, complete};
        this.#State.push(res);
        return res;
    }

    /** Get and return the item from the todo list with matching id */
    static ReadItem(id) {
        return this.#State.find(t => t.id == id);
    }

    /** Overwrite the data for the Todo List item whose id matches 'id' */
    static UpdateData(id, data) {
        if(!data) {
            return false; //if data is null return false
        }
        let todoItem = this.#State.find(t => t.id == id);
        if(!todoItem) {
            return false; //If the todoItem is not found return false
        }
        todoItem.data = data;
        return true;
    }

    /** Set the 'complete' state of the item whose id matches 'id'  */
    static UpdateCompleted(id, completed) {
        let todoItem = this.#State.find(t => t.id == id);
        if(!todoItem) {
            return false
        }
        todoItem.completed = completed;
        return true;
    }

    /** Remove the Todo List item whose id matches 'id' */
    static DeleteItem(id) {
        let s = this.#State.length;
        this.#State = this.#State.filter((t) => t.id != id); //this creates a new array w/out the element of id 'id'
        return this.#State.length == (s - 1);
    }
}