from datetime import datetime, timedelta
import numpy as np
import os

n_users = 512
n_items_per_user = 32
datetime_now = datetime.now()
start_time = datetime_now - timedelta(weeks=26)



headers_filename = "headers.csv"
headers = list(open(headers_filename))
n_items = len(headers)

means_filename = "means.csv"
covar_filename = "covar.csv"
def build_random_model():
    mean_interval = [60 * 60 * 24 * 3, 60 * 60 * 24 * 7 * 2]  # Random mean between 3 days and 2 weeks.
    stdd_interval = [60 * 60 * 24 * 3, 60 * 60 * 24 * 7 * 2]  # Random std.dev between 3 days and 2 weeks.
    means = np.random.uniform(mean_interval[0], mean_interval[1], n_items)
    stdds = np.random.uniform(stdd_interval[0], stdd_interval[1], n_items)

    covar = np.zeros(shape=(n_items, n_items))
    for i in range(n_items):
        covar[i, i] = stdds[i] * stdds[i]
    for i in range(n_items):
        for j in range(n_items):
            cmax = np.sqrt(stdds[i] * stdds[i] * stdds[j] * stdds[j])
            cmin = -cmax
            covar[j, i] = covar[i, j] = np.random.uniform(cmin, cmax)

    np.savetxt(means_filename, means)
    np.savetxt(covar_filename, covar)
    return means, covar

def load_model():
    return np.loadtxt(means_filename), np.loadtxt(covar_filename)


# means, covar = build_random_model()
means, covar = load_model()




def random_time_between(start: datetime, end: datetime) -> datetime:
    p = np.random.uniform()
    return start + p * (end - start)



def build_random_users():
    purchases = np.zeros((n_users, n_items_per_user, 4))
    for i in range(n_users):
        print("User" + str(i))
        user_start_time = random_time_between(start_time, datetime_now)
        user_start_time_seconds = -(datetime_now - user_start_time).total_seconds()

        user_chosen_items = np.random.permutation(np.arange(n_items))[:n_items_per_user]
        select_means = means[user_chosen_items]
        select_covar = covar[user_chosen_items, :][:, user_chosen_items]
        user_means = np.abs(np.random.multivariate_normal(select_means, select_covar))
        user_stdds = (user_means / 2)

        for j in range(n_items_per_user):
            sum_p = 0
            n_p = 0

            ptime = user_start_time_seconds + np.random.normal(user_means[j], user_stdds[j]) * np.random.uniform()
            pnext = np.random.normal(user_means[j], user_stdds[j])
            while pnext <= 0:
                pnext = np.random.normal(user_means[j], user_stdds[j])
            while ptime + pnext < 0:
                n_p += 1
                ptime += pnext
                sum_p += pnext

                pnext = np.random.normal(user_means[j], user_stdds[j])
                while pnext <= 0:
                    pnext = np.random.normal(user_means[j], user_stdds[j])

            if n_p > 0:
                mean_p = sum_p / n_p
            else:
                mean_p = 0

            #last_p = datetime_now + timedelta(seconds=ptime)
            purchases[i, j, :] = [user_chosen_items[j], mean_p, n_p, ptime]

        np.savetxt("Users\\User%d.csv" % i, purchases[i, :, :])

    np.savetxt("allusers.csv", purchases.reshape((n_users * n_items_per_user, 4)))
    return purchases

def load_users():
    users = np.zeros((n_users, n_items_per_user, 4))
    i = 0
    for file in [f for f in os.listdir("Users\\") if f.endswith(".csv")]:
        users[i, :, :] = np.loadtxt(os.path.join("Users\\", file))
        i += 1
    return users

#users = build_random_users()
users = load_users()


items_total = np.zeros((n_items, 2))  # Mean, N
for i in range(n_users):
    for j in range(n_items_per_user):
        k = int(users[i, j, 0])
        items_total[k, 0] += users[i, j, 1]
        items_total[k, 1] += 1
for i in range(n_items):
    if items_total[i, 1] > 0:
        items_total[i, 0] /= items_total[i, 1]
np.savetxt("totals.csv", items_total)

