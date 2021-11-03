import pandas as pd

# pandas
# xlrd

travel_df = pd.read_excel('../travelspots/test.xls')
# print(travel_df)


travel = travel_df.to_dict()

print(travel)
