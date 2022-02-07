import random

# User-defined constants
INPUT_FILE = "data/Q3_large_input1_in.txt"
OUTPUT_FILE = "data/Q3_large_input1_out.txt"
USERS = ["Alice", "Bob", "Charlie", "Dave", "Eve", "Frank", "George", "Helen"]
OTHER_WORDS = ["no", "nobody", "never", "know", "nose", "ever", "who", "what", "when", "where", "how"]
OUTPUT = ["comp", "math", "science", "engineering", "approximation"]
MAX_WORDS_PER_LINE = 5
MAX_CHARS = 2000000

# Derived constants
ALL_WORDS = OTHER_WORDS + OUTPUT
MAX_CHARS_PER_LINE = len(max(USERS, key=len)) + MAX_WORDS_PER_LINE * (1 + len(max(ALL_WORDS, key=len)))
NUM_LINES = MAX_CHARS // MAX_CHARS_PER_LINE

# Frequency of each word (not including the initial required lines)
frequencies = {w: 0 for w in OUTPUT}

shuffled = lambda x: random.sample(x, len(x))
random_user = lambda: USERS[random.randint(0, len(USERS) - 1)]
random_word = lambda: ALL_WORDS[random.randint(0, len(ALL_WORDS) - 1)]

def make_required_line(user):
    return f"{user} {' '.join(shuffled(OUTPUT))}"

def make_random_line():
    user = random_user()
    num_words = random.randint(1, MAX_WORDS_PER_LINE)
    words = []
    for i in range(num_words):
        w = random_word()
        if w in frequencies:
            frequencies[w] += 1
        words.append(w)
    return f"{user} {' '.join(words)}"

def main():
    global USERS
    num_chars = 0
    lines = []
    # Ensure all required words are used by all users
    for u in USERS:
        line = make_required_line(u)
        num_chars += len(line)
        lines.append(line)
    # Remove last user from list to ensure no other words are used by everyone
    USERS = USERS[:-1]
    # Make more random lines
    for line_num in range(NUM_LINES - len(USERS)):
        line = make_random_line()
        num_chars += len(line)
        lines.append(line)
    # Keep adding random lines until the board is close to max number of characters
    while num_chars <= MAX_CHARS - MAX_CHARS_PER_LINE:
        line = make_random_line()
        num_chars += len(line)
        lines.append(line)
    lines = shuffled(lines)
    with open(INPUT_FILE, "w") as f:
        for l in lines:
            f.write(f'{l}\n')
    frequencies_list = list(frequencies.items())
    frequencies_list.sort(key = lambda x: x[1])
    output = [x[0] for x in frequencies_list]
    print(frequencies_list)
    with open(OUTPUT_FILE, "w") as f:
        for w in output:
            f.write(f"{w}\n")

if __name__ == '__main__':
    main()
