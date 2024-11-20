Sample invocation:

    $ scryer-prolog generaton.pl reqs.pl

Sample query:


    ?- requirements_variables(Rs, Vs),
       labeling([ff], Vs),
       print_classes(Rs).
