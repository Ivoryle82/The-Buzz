import {TodoList} from "../models/todoList.js";
import {TodoItem} from "./todoItem.js";

/**
 * AppRoot is a custom HTML5 component. Its style is declared in appRoot.css
 * its structure is declared in appRoot.html
 * 
 * AppRoot will be available to the application via the tag <app-root>.
 * It has two main parts: a list of elements and a button
 * 
 * In order to make this component self-contained, it has a static 'init' function.
 * When initializing a web app, be sure to call this 'init' function to
 * make app-root into a valid HTML tag
 */
export class AppRoot extends HTMLElement {
    //Note that the use of 'templatePath', 'tagName', and 'init' is an attempt to make
    //HTML5 componenets more maintainable. If we had a build tool, like webpack, or a 
    //framework, like react, those frameworks would do this work for us

    /** The html file to use for the template */
    static #templatePath = "src/components/appRoot.html";

    /** The tag name associated with this element */
    static #tagName = "app-root"

    /** The HTML template to use when making this component */
    static #template = undefined;

    /** The HTML tag with all the items in it */
    #items;

    /** The HTML button for adding */
    #addBtn;

    /** Create the app-root tag */
    static async init() {
        // Fetch the template's HTML, convert it to text
        let src = await fetch(AppRoot.#templatePath);
        let txt = await src.text(); //I wonder what this line does, it parses the src HTML into text

        //Inject a template into the document head. The template body is the HTML we just 
        //fetched
        AppRoot.#template = document.createElement("template");
        AppRoot.#template.innerHTML = txt; //now the AppRoot classes template HTML is the HTML defined in appRoot.html
        document.head.appendChild(AppRoot.#template); //When we call init it will add the specifier in the head with this line
        // Associate this type (AppRoot) with the tag "app-root"
        window.customElements.define(AppRoot.#tagName, AppRoot);
    }

    /**
     * Each time there is an app-root tag in our HTML document, this constructor will be
     * called to create the addociated HTML element
     */
    constructor() {
        //Since this extends HTMLElement, we need to make ourselves a proper HTMLElement first
        super();
        
        //The "DOM" (document object model) is just a tree of HTML objects. we attach a
        //"Shadow DOM" to our HTML element, because right now this is just a plain-old
        //HTMLElement, which has nothing in it, what we want is a tree of stuff
        //(the stuff that will be in our appRoot.html file)
        this.attachShadow({mode:"open"});

        // Now that we have a shadow DOM, we can make a copy of our template and set it as
        // the child of our shadow DOM using the shadow clone jutsu hand signs.
        // This injects our HTML into the document, and lets us access it from the shadow DOM.
        // The shadow DOM is self-contained: when we put styles in it, or when we look
        // for HTML elements in it, we start at its root, not the whole document
        this.shadowRoot.appendChild(AppRoot.#template.content.cloneNode(true));


        //Find the div that will hold our todo items, and find the button for
        //adding new todo items to the list. Query the shadow root
        //it associates the created html with the js element
        this.#items = this.shadowRoot.querySelector("div");
        this.#addBtn = this.shadowRoot.querySelector("button");
    }

    /**
     * Whenever an app-root is added to the document, this will be called.
     * Its job is to specify what to do when the "add" button is clicked
     */
    connectedCallback() {
        //When the add button is clicked, call our "addTodoItem" method. and
        //pass it the object describing the click event.
        this.#addBtn.addEventListener("click", e => this.#addTodoItem(e));
    }

    /**
     * In response to a click of our "add" button, add a todo-item tag.
     * 
     * @param e : the click event
     */
    #addTodoItem(e) {
        // We're fully handling the click, so dont pass the click on to other objects under
        // this button
        e.stopPropagation();

        //Get a new object from the model, use it to make a todo-item tag and add it to the list
        let data = TodoList.CreateItem();
        let todoItem = document.createElement(TodoItem.tagName);
        todoItem.setData(data);
        this.#items.appendChild(todoItem);
    }

    /**
     * When an AppRoot tag is removed from the document, this will be called
     * In our app, it doesn't do anything
     */
    disconnectedCallback() { };
}