import os

with open("combine.csv", "w") as f1:
    i = 0
    for fn in [f for f in os.listdir("Users\\") if f.endswith(".csv")]:
        with open(os.path.join("Users\\", fn), "r") as f2:
            for line in f2:
                f1.write(str(i) + " " + line)
            i += 1

