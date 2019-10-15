myInit :: [a] -> [a]
myInit xs = if length xs <= 1 
                then []
                else (head xs):(myInit (tail xs))

myLast :: [a] -> a
myLast xs = if length xs <= 1
                then head xs
                else myLast (tail xs)

myNull :: [a] -> Bool
myNull xs = if length xs == 0
                then True
                else False

myAppend :: [a] -> [a] -> [a]
myAppend xs ys = myAppend' xs
    where myAppend' xs = if length xs == 0
                            then ys
                            else if length xs == 1
                                then (head xs):ys
                                else (head xs):myAppend' (tail xs)

cons' = (\a -> \b -> \f -> f a b)
head' = (\c -> c (\a -> \b -> a))
tail' = (\c -> c (\a -> \b -> b))

f1 :: Bool -> Bool
f1 b = (\x -> x) b 

if' = (\x -> if x then x else x) 
not' = (\x -> not x)