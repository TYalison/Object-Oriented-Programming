#include<stdio.h>
#include<stdlib.h>

#define MAXSIZE 100000000

typedef struct
{
	int coef;
	int expo;
}poly;

int cal(poly *po,int num,char op)
{
	char tmpch;
	int co,ex,flag=0;
	int i=0;
	tmpch=getchar();
	while(tmpch!='}')
	{
		if(tmpch=='{'||tmpch==',')
		{
			scanf("(%d,%d)",&co,&ex);
			co=(op=='+')?co:(-co);
			for(i=0;i<num;i++)
			{
				if(po[i].expo==ex)
				{
					po[i].coef+=co;
					flag=1;
					break;
				}
			}
			if(!flag)
			{
				num++;
				po[i].coef=co;
				po[i].expo=ex;
			}
			flag=0;
		}
		tmpch=getchar();
	}
	return num;
}

int main()
{
	int i=0,j=0;
	int tmpco=0;
	char ch,op='+';
	poly *po;
	po=(poly *)malloc(MAXSIZE*sizeof(poly));
	ch=getchar();
	while(ch!='}')
	{
		if(ch=='-')
		{
			op='-';
			ch=getchar();
		}
		if(ch=='{'||ch==',')
		{
			scanf("(%d,%d)",&tmpco,&po[i].expo);
			po[i].coef=(op=='-')?(-tmpco):tmpco;
			i++;
			ch=getchar();
		}
	}
	op=getchar();
	while(op=='+'||op=='-')
	{
		i=cal(po,i,op);
		op=getchar();
	}
	printf("{");
	for(j=0;j<i;j++)
	{
		if(po[j].coef!=0)
		{
			printf(" (%d,%d) ",po[j].coef,po[j].expo);
		}
	}
	printf("}\n");
	return 0;
}

	
