var
   i, j:integer;
   isPrime:boolean;
begin
  writeln("Prime numbers between 0 and 100 are:");

  for i := 2 to 100 do
  begin
    isPrime := true;

    for j := 2 to (i div 2) do
    begin
        if (i mod j = 0) then begin
            isPrime := false;
        end;
    end;
    if( isPrime) then begin
        writeln(i);
    end;
  end;
end.

