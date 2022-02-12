# Eclipse Solargraph 
<a href="https://marketplace.eclipse.org/content/ruby-solargraph">
<img src ="https://img.shields.io/eclipse-marketplace/v/ruby-solargraph.svg" />
</a>
<a href="https://marketplace.eclipse.org/content/ruby-solargraph">
<img src ="https://img.shields.io/eclipse-marketplace/favorites/ruby-solargraph.svg" />
</a>
<a href="https://marketplace.eclipse.org/content/ruby-solargraph">
<img src ="https://img.shields.io/eclipse-marketplace/dt/ruby-solargraph.svg" />
</a>

**Ruby plugin combining the powers of the Eclipse IDE and the Solargraph language server!**

<p align="center" style="font-size:5px;">
<br />
<img src ="https://github.com/PyvesB/eclipse-solargraph/blob/master/images/editor.png?raw=true" width="49%" />
<img src ="https://github.com/PyvesB/eclipse-solargraph/blob/master/images/debugger.png?raw=true" width="49%" />
<br />
<i><sub>Left: Ruby edition with syntax highlighting, autocomplete, docs, outline, references search and many other features! Right: debugging session!</sub></i>
</p>

## :gem: Features at a glance

* Rich syntax highlighting and code folding
* Code completion
* Documentation hovers
* Rename refactoring
* Find references
* Jump to declarations
* Code outline
* Run files as Ruby scripts with customisable launch configurations
* Run `bundle install` on Gemfiles and `gem build` on gemspec files
* Various other features part of the Language Server Protocol
* Support for Rails and web development with ERB files
* Experimental debugger

Check out what's new in the [latest releases](https://github.com/PyvesB/eclipse-solargraph/releases)!

## :cd: Plugin installation

You can download and install the plugin via the [Eclipse Marketplace](https://marketplace.eclipse.org/content/ruby-solargraph/), or simply drag the below button to your running Eclipse workspace:

<p align="center">
<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=4611382" class="drag" title="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client"><img style="width:80px;" typeof="foaf:Image" class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.svg" alt="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client" /></a>
</p>

Alternatively, head to the [update site](https://pyvesb.github.io/eclipse-solargraph/) and follow the instructions listed there.

Once installed, the plugin will simply kick in when opening any Ruby file in Eclipse; there is no Ruby-specific project type or perspective.

## :sparkles: Tips and tricks

* To improve code completion and intellisense, it is recommended to generate YARD documentation for your gems (run `yard gems` in a terminal).
* You can fine-tune Solargraph's behaviour on a per-project basis by [creating a .solargraph.yml configuration file](https://solargraph.org/guides/configuration).
* If you're doing web development and working on ERB files, it is recommended to also install the [Wild Web Developer](https://github.com/eclipse/wildwebdeveloper) plugin.

## :star: Support and feedback

Found a problem or need some help? Simply open an [**issue**](https://github.com/PyvesB/eclipse-solargraph/issues)!

Find the project useful or interesting? **Star** the repository by clicking on the icon on the top right of this page!

## :computer: Code contributions

Want to make this plugin better, faster, stronger? Contributions are more than welcome, open a **pull request** and share your code!

Setting up your own working copy of the project is easy:
* Download the [RCP and RAP Developers](https://eclipse.org/downloads/eclipse-packages/) version of Eclipse.
* Fork the repository by clicking on the *Fork* icon on the top right of this page and clone it locally.
* In Eclipse, go to `File` -> `Import...` -> `General` -> `Existing Projects into Workspace`.
* In the `Select root directory` field, indicate the location where you checked out the eclipse-solargraph repository.
* Ensure `Search for nested projects` is enabled, select all projects in the `Projects` field and click `Finish`.
* Open `eclipse-solargraph-target-platform.target` and click `Set as Active Target Platform`.
* You're ready to go! You can now either launch an instance of Eclipse running the plugin by right-clicking on the plugin project and selecting `Run As` -> `Eclipse Application`, or you can export a plugin archive file by selecting `Export` -> `Deployable plug-ins and fragments`.

## :balance_scale: License and acknowledgements

Eclipse Solargraph licensed under Eclipse Public License - v 2.0.

The following projects are used by this plugin:
* [Solargraph](http://solargraph.org/) by [castwide](https://github.com/castwide): underlying language server (MIT License) and adapted marketplace logo (CC BY-SA License).
* [Readapt](https://github.com/castwide/readapt) by [castwide](https://github.com/castwide): debugger (MIT License).
* [LSP4E](https://projects.eclipse.org/projects/technology.lsp4e): Language Server Protocol support in the Eclipse IDE (Eclipse Public License).
* [VS Code](https://code.visualstudio.com/): TextMate language configuration (MIT License).
* [TM4E](https://projects.eclipse.org/projects/technology.tm4e): TextMate support in the Eclipse IDE (Eclipse Public License).
* [Ruby](https://www.ruby-lang.org): launch shortcut logo (Creative Commons License).
