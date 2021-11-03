import pandas as pd

travel_df = pd.read_excel('./test.xls')
# print(travel_df)


travel = travel_df.to_dict()

print(travel)
