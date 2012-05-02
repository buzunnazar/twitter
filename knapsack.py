from default_include import *
from copy import deepcopy

class Item:
    def __init__(self, weight, cost):
        self.weight = weight
        self.cost = cost

    def __str__(self):
        return '(' + str(self.weight) + ':' + str(self.cost) + ')'


class Solution:

    @accepts(Self(), [Item])
    def __init__(self, items):
        self.items = items

        self.weight = fsum([item.weight for item in items])
        self.cost = fsum([item.cost for item in items])

    @accepts(Self(), Item)
    def additem(self, item):
        new_S = deepcopy(self)

        new_S.items.append(item)
        new_S.weight += item.weight
        new_S.cost += item.cost

        return new_S

    @accepts(Self(), Self())
    def __add__(self, other):           # self and other should not have equal items
        new_S = deepcopy(self)
        new_S.items += other.items
        new_S.weight += other.weight
        new_S.cost += other.cost

        return  new_S

    def __str__(self):
        res = str()
        res += "sum weight: " + str(self.weight) + "\n"
        res += "sum cost: " + str(self.cost) + "\n"

        res += '['
        for item in self.items:
            res += str(item)

        res += ']'

        return  res



@accepts([Solution], [Solution])
def merge_item_sets(s1, s2): # merge two not empty solution lists
    i = j = 0
    res = []
    while i <= len(s1) or j <= len(s2):
        if i == len(s1):
            res += s2[j:]   # take mod from second list
            break
        if j == len(s2):
            res += s1[i:]   # take mod from first list
            break

        if s1[i].weight <= s2[j].weight:
            if s1[i].cost > s2[j].cost:
                j += 1
            else:
                res.append(s1[i])
                i += 1
        else:

            if s1[i].cost < s2[j].cost:
                i += 1
            else:
                res.append(s2[j])
                j += 1
    return res



@accepts([Item], float)
def KnapsackNemhauserUllman(items, B):
    pareto = [Solution([])]  # pareto optimised solution

    for item in items:
        news = []
        for solution in pareto:
            if solution.weight + item.weight <= B:
                news.append( solution.additem(item) )

        pareto = merge_item_sets(pareto, news)

    return pareto[-1], len(pareto)




def test():
    items = [Item(8,8.1), Item(9,10), Item(10,11) , Item(8,8), Item(8,8), Item(9,10), Item(10,11) , Item(8,8), Item(8,8), Item(9,10), Item(10,11) , Item(8,8), Item(8,8)]
    sol, sol_count =  KnapsackNemhauserUllman(items, 35.0)

    print sol
    print sol_count




#test()

