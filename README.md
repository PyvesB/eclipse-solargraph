# Eclipse Solargraph 
<a href="https://github.com/PyvesB/eclipse-solargraph/blob/master/LICENSE">
<img src ="https://img.shields.io/github/license/PyvesB/eclipse-solargraph.svg" />
</a>
<a href="https://github.com/PyvesB/eclipse-solargraph/issues">
<img src ="https://img.shields.io/github/issues/PyvesB/eclipse-solargraph.svg" />
</a>
<a href="https://github.com/PyvesB/eclipse-solargraph/stargazers">
<img src ="https://img.shields.io/github/stars/PyvesB/eclipse-solargraph.svg" />
</a>
<a href="https://marketplace.eclipse.org/content/eclipse-solargraph">
<img src ="https://img.shields.io/eclipse-marketplace/v/ruby-solargraph.svg" />
</a>
<a href="https://marketplace.eclipse.org/content/eclipse-solargraph">
<img src ="https://img.shields.io/eclipse-marketplace/favorites/ruby-solargraph.svg" />
</a>

**Ruby plugin combining the powers of the Eclipse IDE and the Solargraph language server!**

<p align="center" style="font-size:5px">
<img src ="https://github.com/PyvesB/eclipse-solargraph/blob/master/images/screenshot.png?raw=true" />
<br />
<i><sub>Ruby edition with syntax highlighting and autocomplete. Many other features are also available.</sub></i>
</p>

# Features at a glance

* Rich syntax highlighting
* Code completion
* Rename refactoring
* Find references
* Jump to declaration
* Code outline
* Code folding
* Documentation hovers, see the [Solargraph readme](https://github.com/castwide/solargraph#gem-support) for more information
* Run files as Ruby programs
* Run `bundle install` on Gemfiles and `gem build` on gemspec files
* Various other features part of the Language Server Protocol

Check out what's new in the [latest releases](https://github.com/PyvesB/eclipse-solargraph/releases)!

# Getting started

#### :cd: Plugin installation

You can download and install the plugin via the [Eclipse Marketplace](https://marketplace.eclipse.org/content/ruby-solargraph/), or simply drag the below button to your running Eclipse workspace:

<p align="center">
<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=4611382" class="drag" title="Drag to your running Eclipse workspace. Requires Eclipse Marketplace Client"><img typeof="foaf:Image" class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png" alt="Drag to your running Eclipse workspace. Requires Eclipse Marketplace Client" /></a>
</p>

Alternatively, head to the [update site](https://pyvesb.github.io/eclipse-solargraph/) and follow the instructions listed there.

#### :wrench: Setting your own working copy of the project

* Download the [RCP and RAP Developers](https://eclipse.org/downloads/eclipse-packages/) version of Eclipse.
* Clone or download this repository. You can also create your own fork by clicking on the *Fork* icon on the top right of this page.
* In Eclipse, go to `File` -> `Import...` -> `General` -> `Existing Projects into Workspace`.
* In the `Select root directory` field, indicate the location where you checked out the eclipse-solargraph repository.
* Ensure `Search for nested projects` is enabled, select all projects in the `Projects` field and click `Finish`.
* You're ready to go! You can now either launch an instance of Eclipse running the plugin by right-clicking on the plugin project and selecting `Run As` -> `Eclipse Application`, or you can export a plugin archive file by selecting `Export` -> `Deployable plug-ins and fragments`.

# Contributing

#### `$ code`

Want to make this plugin better, faster, stronger? Contributions are more than welcome, open a **pull request** and share your code! Simply **fork** the repository by clicking on the icon on the top right of this page and you're ready to go!

#### :speech_balloon: Support

Thought of a cool idea? Found a problem or need some help? Simply open an [**issue**](https://github.com/PyvesB/eclipse-solargraph/issues)!

#### :star: Thanks

Find the project useful, fun or interesting? **Star** the repository by clicking on the icon on the top right of this page!

# Acknowledgements

The following projects are used by this plugin:
* [Solargraph](http://solargraph.org/) by [castwide](https://github.com/castwide): underlying language server (MIT License) and adapted marketplace logo (CC BY-SA License).
* [LSP4E](https://projects.eclipse.org/projects/technology.lsp4e): Language Server Protocol support in the Eclipse IDE (Eclipse Public License).
* [VS Code](https://code.visualstudio.com/): TextMate language configuration (MIT License).
* [TM4E](https://projects.eclipse.org/projects/technology.tm4e): TextMate support in the Eclipse IDE (Eclipse Public License).
* [Ruby](https://www.ruby-lang.org): launch shortcut logo (Creative Commons License).

# License 

Eclipse Public License - v 2.0
