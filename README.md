# comp251-a1-tester
This is a collaborative student tester for assignment 1 of COMP 251: Data Structures and Algorithms.

## Installation
1. Clone or download the entire test repository.
	- Cloning the repository is the recommended option if you're familiar with Git, as it will allow you to more easily stay up to date as more tests are added. To do so, move to the src/ folder of your assignment and run the command
        ```sh
        git clone https://github.com/louis-hildebrand/comp251-a1-tester.git test
        ```
	- Otherwise, download the entire repository by going to https://github.com/louis-hildebrand/comp251-a1-tester, pressing the big green button, and pressing "Download ZIP." Unzip the test folder into `src/test` in your assignment folder. Make sure that the `test` folder isn't duplicated (i.e. `src/test/test`). See the example below for details.
2. Ensure the project packages are named correctly. The tester assumes that it is in the `src/test` folder and that your assignment code is in the `src/main` folder. In other words, your project setup should look something like this:
    ```
    .
    ├── Instructions.pdf
    ├── src
    │   ├── main
    │   │   ├── A1_Q3.java
    │   │   ├── Chaining.java
    │   │   ├── DisjointSets.java
    │   │   └── Open_Addressing.java
    │   └── test
    │       ├── README.md
    │       ├── TestHelper.java
    │       ├── Tester.java
    │       ├── data
    │       │   ├── Q3_large_input1_in.txt
    │       │   └── Q3_large_input1_out.txt
    │       └── q3_make_discussion_board.py
    └── unionfind.txt
    ```

## Running the tests
To run the tests, run `Tester.java`. This should tell you which tests are failing and provide some information on what's wrong.

## Contributing
- If you notice a bug, please open an issue and describe the problem: https://github.com/louis-hildebrand/comp251-a1-tester/issues/new.
- If you'd like to add your own tests, please fork the GitHub repository, make your changes, and open a pull request. I'll do my best to review the changes promptly.
    - ___IMPORTANT:___ Only add test code here, NEVER assignment code.
